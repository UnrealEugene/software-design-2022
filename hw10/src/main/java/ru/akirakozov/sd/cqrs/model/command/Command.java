package ru.akirakozov.sd.cqrs.model.command;

import ru.akirakozov.sd.cqrs.model.event.Event;
import ru.akirakozov.sd.cqrs.service.AggregatorService;

import java.util.List;

public interface Command {
    List<Event> handleSelf(AggregatorService aggregate);
}
