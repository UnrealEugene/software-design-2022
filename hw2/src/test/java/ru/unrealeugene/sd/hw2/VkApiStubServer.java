package ru.unrealeugene.sd.hw2;

import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.mockserver.model.Parameter;

import java.util.List;

public class VkApiStubServer {
    private MockServerClient stubServer = null;

    public void start(int port) {
        stubServer = ClientAndServer.startClientAndServer(port);
    }

    public void stop() {
        stubServer.stop();
    }

    public void mockApiMethod(byte[] body, String method, List<Parameter> params) {
        stubServer
                .when(HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/method/" + method)
                        .withQueryStringParameters(params))
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody(body));
    }
}
