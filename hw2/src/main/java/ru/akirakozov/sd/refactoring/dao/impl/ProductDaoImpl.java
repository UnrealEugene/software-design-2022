package ru.akirakozov.sd.refactoring.dao.impl;

import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.domain.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ProductDaoImpl implements ProductDao {
    private final Properties properties;

    public ProductDaoImpl(Properties properties) {
        this.properties = properties;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(properties.getProperty("database.url"));
    }

    @Override
    public void createTableIfNotExists() {
        try (Connection c = getConnection()) {
            Statement stmt = c.createStatement();
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS PRODUCT
                    (ID    INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                     NAME  TEXT    NOT NULL,
                     PRICE INT     NOT NULL)""");
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insert(Product product) {
        try {
            try (Connection c = getConnection()) {
                PreparedStatement stmt = c.prepareStatement("INSERT INTO PRODUCT (NAME, PRICE) VALUES (?, ?)");

                stmt.setString(1, product.getName());
                stmt.setLong(2, product.getPrice());

                stmt.executeUpdate();
                stmt.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Product> findAll() {
        try {
            try (Connection c = getConnection()) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT");
                List<Product> products = collectProductListFromResultSet(rs);

                rs.close();
                stmt.close();

                return products;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product findMax() {
        try {
            try (Connection c = DriverManager.getConnection(properties.getProperty("database.url"))) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");
                Product product = collectProductListFromResultSet(rs).get(0);

                rs.close();
                stmt.close();

                return product;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product findMin() {
        try {
            try (Connection c = DriverManager.getConnection(properties.getProperty("database.url"))) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");
                Product product = collectProductListFromResultSet(rs).get(0);

                rs.close();
                stmt.close();

                return product;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long findSum() {
        try {
            try (Connection c = DriverManager.getConnection(properties.getProperty("database.url"))) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT SUM(price) FROM PRODUCT");

                long sum = 0;
                if (rs.next()) {
                    sum = rs.getLong(1);
                }

                rs.close();
                stmt.close();

                return sum;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long findCount() {
        try {
            try (Connection c = DriverManager.getConnection(properties.getProperty("database.url"))) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM PRODUCT");

                long cnt = 0;
                if (rs.next()) {
                    cnt = rs.getLong(1);
                }

                rs.close();
                stmt.close();

                return cnt;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<Product> collectProductListFromResultSet(ResultSet rs) throws SQLException {
        List<Product> products = new ArrayList<>();
        while (rs.next()) {
            String name = rs.getString("name");
            int price = rs.getInt("price");
            products.add(new Product(name, price));
        }
        return products;
    }
}
