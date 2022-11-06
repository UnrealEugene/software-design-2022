package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Properties;

/**
 * @author akirakozov
 */
public class AddProductServlet extends HttpServlet {
    private final Properties properties;

    public AddProductServlet(Properties properties) {
        this.properties = properties;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));

        try {
            try (Connection c = DriverManager.getConnection(properties.getProperty("database.url"))) {
                PreparedStatement stmt = c.prepareStatement("INSERT INTO PRODUCT (NAME, PRICE) VALUES (?, ?)");

                stmt.setString(1, name);
                stmt.setLong(2, price);

                stmt.executeUpdate();
                stmt.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("OK");
    }
}
