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
import ru.akirakozov.sd.reactive.model.Product;
import ru.akirakozov.sd.reactive.model.User;
import ru.akirakozov.sd.reactive.model.view.ProductView;
import ru.akirakozov.sd.reactive.repository.ProductRepository;
import ru.akirakozov.sd.reactive.repository.UserRepository;
import ru.akirakozov.sd.reactive.service.CurrencyExchangeService;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class ProductHandler {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CurrencyExchangeService currencyExchangeService;

    @NonNull
    public Mono<ServerResponse> getAllProducts(ServerRequest request) {
        Mono<UUID> userIdMono = Mono.justOrEmpty(request.queryParam("userId")).map(UUID::fromString);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(
                        currencyExchangeService.projectOnCurrency(
                                productRepository.findAll(),
                                userRepository.findById(userIdMono)
                                        .map(User::getCurrency)
                                        .switchIfEmpty(Mono.just(Currency.USD))
                        ),
                        ProductView.class
                ));
    }

    @NonNull
    public Mono<ServerResponse> addProduct(ServerRequest request) {
        Mono<Product> productMono = Mono.zip(
                Mono.justOrEmpty(request.queryParam("name")),
                Mono.justOrEmpty(request.queryParam("priceUsd")).map(Double::parseDouble).onErrorComplete(),
                (name, priceUsd) -> new Product(UUID.randomUUID(), name, priceUsd)
        );
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(
                        productRepository.saveAll(productMono),
                        Product.class
                ));
    }
}
