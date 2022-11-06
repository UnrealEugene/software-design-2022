package ru.akirakozov.sd.refactoring;

import org.jsoup.Jsoup;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WebServerTest {
    private static final int SERVER_PORT = 11235;
    private static final String TEMP_FILE_PREFIX = "web-server-test-";
    private static final String TEMP_FILE_SUFFIX = ".db";

    private static Path sqliteFilePath;
    private static final HttpClient client = HttpClient.newHttpClient();
    private static WebServer server;

    @BeforeClass
    public static void startServer() throws Exception {
        sqliteFilePath = Files.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX);

        Properties properties = new Properties();
        properties.setProperty("database.url", "jdbc:sqlite:" + sqliteFilePath.toAbsolutePath());

        server = new WebServer(SERVER_PORT, properties);
        server.start();
    }

    @AfterClass
    public static void stopServer() throws Exception {
        server.stop();
        Files.delete(sqliteFilePath);
    }

    @Test
    public void stage01_addProductsTest() throws Exception {
        List<String> requestsUriPatterns = List.of(
                "http://localhost:%d/add-product?name=iphone6&price=300",
                "http://localhost:%d/add-product?name=iphone10&price=1000"
        );

        for (String uriPattern : requestsUriPatterns) {
            assertOkAndGetRequest(uriPattern);
        }
    }

    @Test
    public void stage02_checkProductsTest() throws Exception {
        assertOkAndCompareResponse(
                "http://localhost:%d/get-products",
                "iphone6 300 iphone10 1000"
        );
    }

    @Test
    public void stage03_checkQueryTest() throws Exception {
        assertOkAndCompareResponse(
                "http://localhost:%d/query?command=max",
                "Product with max price: iphone10 1000"
        );

        assertOkAndCompareResponse(
                "http://localhost:%d/query?command=min",
                "Product with min price: iphone6 300"
        );

        assertOkAndCompareResponse(
                "http://localhost:%d/query?command=sum",
                "Summary price: 1300"
        );

        assertOkAndCompareResponse(
                "http://localhost:%d/query?command=count",
                "Number of products: 2"
        );
    }

    private void assertOkAndCompareResponse(String uriPattern, String expectedText) throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> response = assertOkAndGetRequest(uriPattern);
        Assert.assertEquals(expectedText, Jsoup.parse(response.body()).text());
    }

    private HttpResponse<String> assertOkAndGetRequest(String uriPattern) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(new URI(String.format(uriPattern, SERVER_PORT))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(200, response.statusCode());
        return response;
    }
}
