package ru.akirakozov.sd.refactoring;

import java.util.Properties;

/**
 * @author akirakozov
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.setProperty("database.url", "jdbc:sqlite:test.db");

        WebServer server = new WebServer(8081, properties);
        server.start();
        server.join();
    }
}
