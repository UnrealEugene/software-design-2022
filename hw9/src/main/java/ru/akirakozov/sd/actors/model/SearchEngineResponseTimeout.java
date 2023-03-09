package ru.akirakozov.sd.actors.model;

import lombok.Data;

@Data
public final class SearchEngineResponseTimeout implements MasterActorCommand {
    private final String engine;
}
