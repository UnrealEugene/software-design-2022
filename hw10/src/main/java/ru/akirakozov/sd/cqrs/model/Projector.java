package ru.akirakozov.sd.cqrs.model;

import ru.akirakozov.sd.cqrs.model.event.*;

import java.util.List;

public interface Projector<T> {
    default T project(T initialValue, List<Event> events) {
        for (Event event : events) {
            initialValue = event.applySelf(initialValue, this);
        }
        return initialValue;
    }

    T apply(T value, SubscriptionCreatedEvent event);
    T apply(T value, SubscriptionExtendedEvent event);
    T apply(T value, SubscriptionCancelledEvent event);
    T apply(T value, VisitStartedEvent event);
    T apply(T value, VisitEndedEvent event);
}
