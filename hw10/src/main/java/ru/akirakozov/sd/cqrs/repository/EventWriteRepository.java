package ru.akirakozov.sd.cqrs.repository;

import ru.akirakozov.sd.cqrs.model.event.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventWriteRepository {
    void addEvent(Event event);

    default void addEvents(List<Event> events) {
        for (Event event : events) {
            addEvent(event);
        }
    }

    List<Event> getAllEvents();
    long getFreeSubscriptionId();
    String getPasswordHash(long subscriptionId);
    boolean canStartVisit(long subscriptionId, LocalDateTime now);
}
