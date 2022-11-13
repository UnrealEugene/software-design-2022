package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.domain.Product;
import ru.akirakozov.sd.refactoring.util.ResponseHtmlUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    private final ProductDao productDao;

    public QueryServlet(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        switch (command) {
            case "max" -> {
                Product product = productDao.findMax();
                response.getWriter().println(ResponseHtmlUtil.productToHtml("Product with max price", product));
            }
            case "min" -> {
                Product product = productDao.findMin();
                response.getWriter().println(ResponseHtmlUtil.productToHtml("Product with min price", product));
            }
            case "sum" -> {
                long sum = productDao.findSum();
                response.getWriter().println(ResponseHtmlUtil.statisticToHtml("Summary price", sum));
            }
            case "count" -> {
                long cnt = productDao.findCount();
                response.getWriter().println(ResponseHtmlUtil.statisticToHtml("Number of products", cnt));
            }
            default -> response.getWriter().println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
