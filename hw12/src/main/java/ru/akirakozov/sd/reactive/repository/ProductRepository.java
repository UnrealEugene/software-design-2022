package ru.akirakozov.sd.reactive.repository;


import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.akirakozov.sd.reactive.model.Product;

import java.util.UUID;

public interface ProductRepository extends ReactiveMongoRepository<Product, UUID> {

}
