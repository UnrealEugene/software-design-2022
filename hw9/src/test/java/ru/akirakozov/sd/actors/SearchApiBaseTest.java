package ru.akirakozov.sd.actors;

import akka.actor.testkit.typed.javadsl.ActorTestKit;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import ru.akirakozov.sd.actors.server.SearchApiStubServer;
import ru.akirakozov.sd.actors.util.PropertyUtil;

public abstract class SearchApiBaseTest {
    protected static final String HOST = "http://localhost";
    protected static final int PORT = 11235;

    protected static final SearchApiStubServer stubServer = new SearchApiStubServer();
    protected final ActorTestKit testKit = ActorTestKit.create();

    @BeforeClass
    public static void beforeClass() {
        stubServer.start(PORT);
        PropertyUtil.setProperty("serpapi.url", HOST + ":" + PORT);
        PropertyUtil.setProperty("serpapi.private_key", "");
    }

    @AfterClass
    public static void afterClass() {
        stubServer.stop();
    }

    @After
    public void after() {
        testKit.shutdownTestKit();
    }
}
