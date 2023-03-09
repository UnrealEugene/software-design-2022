package ru.akirakozov.sd.actors;

import akka.actor.setup.ActorSystemSetup;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.ExtensionSetup;
import org.junit.Assert;
import org.junit.Test;
import org.mockserver.model.Parameter;
import ru.akirakozov.sd.actors.actor.MasterActor;
import ru.akirakozov.sd.actors.model.MasterActorCommand;
import ru.akirakozov.sd.actors.model.SearchEngineResponse;
import ru.akirakozov.sd.actors.model.SearchRequest;
import ru.akirakozov.sd.actors.model.SearchResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class SearchApiTimeoutTest extends SearchApiBaseTest {
    private static final String SEARCH_QUERY = "coffee";
    private static final int SEARCH_RESULT_COUNT = 5;
    private static final Duration SEARCH_TIMEOUT = Duration.ofSeconds(1);

    @Test
    public void test() throws IOException {
        for (String searchEngine : MasterActor.SEARCH_ENGINES) {
            try (InputStream stream =
                         SearchApiSuccessTest.class.getResourceAsStream("/tests/" + searchEngine + ".json")) {
                byte[] body = Objects.requireNonNull(stream).readAllBytes();

                stubServer.mockApiDelayed(body, List.of(
                        Parameter.param("engine", searchEngine)
                ), SEARCH_TIMEOUT.multipliedBy(3));
            }
        }

        TestProbe<SearchResponse> probe = testKit.createTestProbe();
        ActorRef<MasterActorCommand> masterActor = testKit.spawn(MasterActor.create(probe.getRef()));
        masterActor.tell(new SearchRequest(SEARCH_QUERY, SEARCH_RESULT_COUNT, SEARCH_TIMEOUT));
        SearchResponse response = probe.receiveMessage(SEARCH_TIMEOUT.multipliedBy(2));

        Assert.assertTrue(response.getEngineResponses().isEmpty());
    }
}
