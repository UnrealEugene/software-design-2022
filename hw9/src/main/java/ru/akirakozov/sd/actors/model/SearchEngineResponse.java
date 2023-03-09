package ru.akirakozov.sd.actors.model;

import lombok.Data;

import java.util.List;

@Data
public final class SearchEngineResponse implements MasterActorCommand {
    private final String engine;
    private final List<Item> results;

    @Data
    public static final class Item {
        private final String title;
        private final String link;
    }
}
