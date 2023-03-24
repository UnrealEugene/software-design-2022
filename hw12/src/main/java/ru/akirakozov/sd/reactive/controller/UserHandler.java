package ru.akirakozov.sd.reactive.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.akirakozov.sd.reactive.model.Currency;
import ru.akirakozov.sd.reactive.model.User;
import ru.akirakozov.sd.reactive.repository.UserRepository;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class UserHandler {
    private final UserRepository userRepository;

    @NonNull
    public Mono<ServerResponse> registerUser(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(
                        Mono.justOrEmpty(request.queryParam("currency"))
                                .map(Currency::valueOf)
                                .switchIfEmpty(Mono.just(Currency.USD))
                                .flatMap(currency -> userRepository.save(new User(UUID.randomUUID(), currency))),
                        User.class
                ));
    }

    @NonNull
    public Mono<ServerResponse> getUser(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(
                        Mono.justOrEmpty(request.queryParam("id"))
                                .map(UUID::fromString)
                                .flatMap(userRepository::findById),
                        User.class
                ));
    }
}
