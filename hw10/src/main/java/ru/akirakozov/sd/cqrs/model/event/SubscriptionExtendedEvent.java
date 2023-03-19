package ru.akirakozov.sd.cqrs.model.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.akirakozov.sd.cqrs.model.Projector;

import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
public final class SubscriptionExtendedEvent extends Event {
    private final long subscriptionId;
    private final LocalDate newDueDate;

    @Override
    public <T> T applySelf(T value, Projector<T> projector) {
        return projector.apply(value, this);
    }
}
