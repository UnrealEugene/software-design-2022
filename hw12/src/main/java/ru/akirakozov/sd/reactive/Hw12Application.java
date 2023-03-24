package ru.akirakozov.sd.reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class Hw12Application {
    public static void main(String[] args) {
        SpringApplication.run(Hw12Application.class, args);
    }
}