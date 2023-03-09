package ru.akirakozov.sd.actors.model;

import lombok.Data;

@Data
public final class SearchEngineRequest implements ChildActorCommand {
    private final String engine;
    private final SearchRequest searchRequest;
}
