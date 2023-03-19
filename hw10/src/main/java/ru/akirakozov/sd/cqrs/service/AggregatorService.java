package ru.akirakozov.sd.cqrs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akirakozov.sd.cqrs.model.command.*;
import ru.akirakozov.sd.cqrs.model.event.*;
import ru.akirakozov.sd.cqrs.repository.EventWriteRepository;
import ru.akirakozov.sd.cqrs.util.SubscriptionUtil;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AggregatorService {
    private final EventWriteRepository writeRepository;

    public List<Event> handle(CreateSubscriptionCommand command) {
        long subscriptionId = writeRepository.getFreeSubscriptionId();
        String passwordHash = SubscriptionUtil.getPasswordHash(command.getPassword());
        Event event = new SubscriptionCreatedEvent(subscriptionId, command.getName(), passwordHash, command.getDueDate());
        return List.of(event);
    }

    public List<Event> handle(ExtendSubscriptionCommand command) {
        Event event = new SubscriptionExtendedEvent(command.getSubscriptionId(), command.getNewDueDate());
        return List.of(event);
    }

    public List<Event> handle(CancelSubscriptionCommand command) {
        Event event = new SubscriptionCancelledEvent(command.getSubscriptionId());
        return List.of(event);
    }

    public List<Event> handle(StartVisitCommand command) {
        if (!writeRepository.canStartVisit(command.getSubscriptionId(), LocalDateTime.now())) {
            return List.of();
        }
        String passwordHash = SubscriptionUtil.getPasswordHash(command.getPassword());
        if (!passwordHash.equals(writeRepository.getPasswordHash(command.getSubscriptionId()))) {
            return List.of();
        }
        Event event = new VisitStartedEvent(command.getSubscriptionId());
        return List.of(event);
    }

    public List<Event> handle(EndVisitCommand command) {
        Event event = new VisitEndedEvent(command.getSubscriptionId());
        return List.of(event);
    }
}
