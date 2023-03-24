package ru.akirakozov.sd.reactive.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration
public class Router {
    @Bean
    public RouterFunction<ServerResponse> routeProduct(ProductHandler handler) {
        return RouterFunctions
                .route(GET("/api/product/getAll"), handler::getAllProducts)
                .andRoute(POST("/api/product/add"), handler::addProduct);
    }

    @Bean
    public RouterFunction<ServerResponse> routeUser(UserHandler handler) {
        return RouterFunctions
                .route(GET("/api/user/get"), handler::getUser)
                .andRoute(POST("/api/user/register"), handler::registerUser);
    }
}
