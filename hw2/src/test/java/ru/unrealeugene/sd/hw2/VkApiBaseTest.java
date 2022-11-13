package ru.unrealeugene.sd.hw2;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class VkApiBaseTest {
    protected static final String HOST = "http://localhost";
    protected static final int PORT = 11235;

    protected static final VkApiStubServer stubServer = new VkApiStubServer();

    @BeforeClass
    public static void before() {
        stubServer.start(PORT);
    }

    @AfterClass
    public static void after() {
        stubServer.stop();
    }
}
