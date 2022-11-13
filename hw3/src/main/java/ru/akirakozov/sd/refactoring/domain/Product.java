package ru.akirakozov.sd.refactoring.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Product {
    private final String name;
    private final long price;
}