package ru.akirakozov.sd.refactoring.dao;

import ru.akirakozov.sd.refactoring.domain.Product;

import java.util.List;

public interface ProductDao {
    void createTableIfNotExists();
    void insert(Product product);
    List<Product> findAll();
    Product findMax();
    Product findMin();
    long findSum();
    long findCount();
}
