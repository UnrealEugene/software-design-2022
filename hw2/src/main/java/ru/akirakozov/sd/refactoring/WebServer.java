package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.dao.impl.ProductDaoImpl;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;

import java.sql.SQLException;
import java.util.Properties;

public class WebServer {
    private final Server server;

    public WebServer(int port, Properties properties) throws SQLException {
        ProductDao productDao = new ProductDaoImpl(properties);
        productDao.createTableIfNotExists();

        server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new AddProductServlet(productDao)), "/add-product");
        context.addServlet(new ServletHolder(new GetProductsServlet(productDao)),"/get-products");
        context.addServlet(new ServletHolder(new QueryServlet(productDao)),"/query");
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
