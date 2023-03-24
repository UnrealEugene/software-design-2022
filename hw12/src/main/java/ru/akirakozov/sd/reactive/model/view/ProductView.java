package ru.akirakozov.sd.reactive.model.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductView {
    private UUID id;
    private String name;
    private double price;
}
