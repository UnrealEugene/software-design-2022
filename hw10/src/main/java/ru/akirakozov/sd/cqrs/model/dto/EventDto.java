package ru.akirakozov.sd.cqrs.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public final class EventDto {
    private final UUID id;
    private final LocalDateTime creationTime;
    private final String jsonData;
}
