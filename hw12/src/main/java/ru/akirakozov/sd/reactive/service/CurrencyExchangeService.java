package ru.akirakozov.sd.reactive.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.akirakozov.sd.reactive.model.Currency;
import ru.akirakozov.sd.reactive.model.Product;
import ru.akirakozov.sd.reactive.model.view.ProductView;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Service
public class CurrencyExchangeService {
    private final Gson gson;

    @Value("${open_exchange_rates.app_id}")
    private String appId;

    private WebClient webClient;

    @PostConstruct
    public void postConstruct() {
        webClient = WebClient.create("https://openexchangerates.org/api/latest.json?app_id=" + appId);
    }

    public Flux<ProductView> projectOnCurrency(Flux<Product> productFlux, Mono<Currency> currencyMono) {
        return Flux.zip(productFlux, getCurrencyRateToUsd(currencyMono), (product, rate) ->
                new ProductView(product.getId(), product.getName(), product.getPriceUsd() * rate));
    }

    public Mono<Double> getCurrencyRateToUsd(Mono<Currency> currencyMono) {
        return currencyMono.flatMap(currency -> webClient.get()
                .retrieve()
                .bodyToMono(String.class)
                .map(json -> gson.fromJson(json, JsonObject.class)
                        .getAsJsonObject("rates")
                        .get(currency.toString())
                        .getAsDouble()));
    }
}
