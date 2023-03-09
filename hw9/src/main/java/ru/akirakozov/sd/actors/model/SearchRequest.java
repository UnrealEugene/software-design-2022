package ru.akirakozov.sd.actors.model;

import lombok.Data;

import java.time.Duration;

@Data
public final class SearchRequest implements MasterActorCommand {
    private final String query;
    private final int resultCount;
    private final Duration timeout;
}
