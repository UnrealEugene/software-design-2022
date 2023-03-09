package ru.akirakozov.sd.actors.server;

import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.*;

import java.time.Duration;
import java.util.List;

public class SearchApiStubServer {
    private MockServerClient stubServer = null;

    public void start(int port) {
        stubServer = ClientAndServer.startClientAndServer(port);
    }

    public void stop() {
        stubServer.stop();
    }

    public void mockApi(byte[] body, List<Parameter> params) {
        mockApiDelayed(body, params, Duration.ZERO);
    }

    public void mockApiDelayed(byte[] body, List<Parameter> params, Duration delay) {
        stubServer
                .when(HttpRequest.request()
                        .withMethod("GET")
                        .withQueryStringParameters(params))
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody(body)
                        .withDelay(Delay.milliseconds(delay.toMillis())));
    }
}
