package ru.akirakozov.sd.cqrs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akirakozov.sd.cqrs.model.AbstractProjector;
import ru.akirakozov.sd.cqrs.model.event.Event;
import ru.akirakozov.sd.cqrs.model.event.VisitEndedEvent;
import ru.akirakozov.sd.cqrs.model.event.VisitStartedEvent;
import ru.akirakozov.sd.cqrs.repository.EventWriteRepository;
import ru.akirakozov.sd.cqrs.repository.impl.EventReadRepositoryImpl;

import javax.annotation.PostConstruct;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProjectorService extends AbstractProjector<Void> {
    private final EventWriteRepository writeRepository;
    private final EventReadRepositoryImpl readRepository;

    @PostConstruct
    private void postConstruct() {
        project(null, writeRepository.getAllEvents());
    }

    public void project(List<Event> events) {
        project(null, events);
    }

    @Override
    public Void apply(Void ignored, VisitStartedEvent event) {
        readRepository.startVisit(event.getSubscriptionId(), event.getCreationTime());
        return null;
    }

    @Override
    public Void apply(Void ignored, VisitEndedEvent event) {
        readRepository.endVisit(event.getSubscriptionId(), event.getCreationTime());
        return null;
    }
}
