package ru.akirakozov.sd.cqrs.model.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.akirakozov.sd.cqrs.model.event.Event;
import ru.akirakozov.sd.cqrs.service.AggregatorService;

import java.util.List;

@RequiredArgsConstructor
@Getter
public final class StartVisitCommand implements Command {
    private final long subscriptionId;
    private final String password;

    @Override
    public List<Event> handleSelf(AggregatorService aggregate) {
        return aggregate.handle(this);
    }
}
