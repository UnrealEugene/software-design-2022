package ru.akirakozov.sd.cqrs.model.event;

import lombok.Getter;
import ru.akirakozov.sd.cqrs.model.Projector;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public abstract class Event {
    private final UUID id = UUID.randomUUID();
    private final LocalDateTime creationTime = LocalDateTime.now();

    public abstract <T> T applySelf(T value, Projector<T> projector);
}
