package ru.akirakozov.sd.cqrs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akirakozov.sd.cqrs.model.command.Command;
import ru.akirakozov.sd.cqrs.model.event.Event;
import ru.akirakozov.sd.cqrs.repository.EventWriteRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EventService {
    private final EventWriteRepository eventWriteRepository;
    private final AggregatorService aggregatorService;
    private final ProjectorService projectorService;

    public void send(Command command) {
        List<Event> events = command.handleSelf(aggregatorService);
        eventWriteRepository.addEvents(events);
        projectorService.project(events);
    }
}
