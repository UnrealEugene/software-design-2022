package ru.akirakozov.sd.reactive.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.akirakozov.sd.reactive.model.User;

import java.util.UUID;

public interface UserRepository extends ReactiveMongoRepository<User, UUID> {

}
