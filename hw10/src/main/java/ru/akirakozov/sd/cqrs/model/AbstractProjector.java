package ru.akirakozov.sd.cqrs.model;

import ru.akirakozov.sd.cqrs.model.event.*;

public class AbstractProjector<T> implements Projector<T> {
    public T apply(T value, SubscriptionCreatedEvent event) {
        return value;
    }

    public T apply(T value, SubscriptionExtendedEvent event) {
        return value;
    }

    public T apply(T value, SubscriptionCancelledEvent event) {
        return value;
    }

    public T apply(T value, VisitStartedEvent event) {
        return value;
    }

    public T apply(T value, VisitEndedEvent event) {
        return value;
    }
}
