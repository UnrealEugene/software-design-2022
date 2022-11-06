package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class WebServer {
    private final Server server;

    public WebServer(int port, Properties properties) throws SQLException {
        try (Connection c = DriverManager.getConnection(properties.getProperty("database.url"))) {
            String sql = """
                CREATE TABLE IF NOT EXISTS PRODUCT
                    (ID    INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                     NAME  TEXT    NOT NULL,
                     PRICE INT     NOT NULL)""";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }

        server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new AddProductServlet(properties)), "/add-product");
        context.addServlet(new ServletHolder(new GetProductsServlet(properties)),"/get-products");
        context.addServlet(new ServletHolder(new QueryServlet(properties)),"/query");
    }

    public void start() throws Exception {
        server.start();
    }

    public void join() throws InterruptedException {
        server.join();
    }

    public void stop() throws Exception {
        server.stop();
    }
}
