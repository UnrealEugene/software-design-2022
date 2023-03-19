package ru.akirakozov.sd.cqrs.model.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.akirakozov.sd.cqrs.model.Projector;

@RequiredArgsConstructor
@Getter
public final class SubscriptionCancelledEvent extends Event {
    private final long subscriptionId;

    @Override
    public <T> T applySelf(T value, Projector<T> projector) {
        return projector.apply(value, this);
    }
}
