package ru.akirakozov.sd.actors.model;

import lombok.Data;

import java.util.List;

@Data
public final class SearchResponse {
    private final List<SearchEngineResponse> engineResponses;
}
