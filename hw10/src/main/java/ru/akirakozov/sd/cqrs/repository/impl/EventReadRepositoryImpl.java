package ru.akirakozov.sd.cqrs.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.akirakozov.sd.cqrs.model.AbstractProjector;
import ru.akirakozov.sd.cqrs.model.event.SubscriptionCancelledEvent;
import ru.akirakozov.sd.cqrs.model.event.SubscriptionCreatedEvent;
import ru.akirakozov.sd.cqrs.model.event.SubscriptionExtendedEvent;
import ru.akirakozov.sd.cqrs.model.view.SubscriptionInfoView;
import ru.akirakozov.sd.cqrs.model.view.VisitReportView;
import ru.akirakozov.sd.cqrs.repository.EventReadRepository;
import ru.akirakozov.sd.cqrs.repository.EventWriteRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class EventReadRepositoryImpl implements EventReadRepository {
    private static final Map<LocalDate, Long> VISIT_STATISTICS = new HashMap<>();
    private static final Map<Long, LocalDateTime> CURRENTLY_VISITING = new HashMap<>();

    private static Duration visitTimeSum = Duration.ZERO;

    private final EventWriteRepository writeRepository;

    @Override
    public SubscriptionInfoView getSubscriptionInfo(long subscriptionId) {
        return new AbstractProjector<SubscriptionInfoView>() {
            @Override
            public SubscriptionInfoView apply(SubscriptionInfoView value, SubscriptionCreatedEvent event) {
                if (event.getSubscriptionId() == subscriptionId) {
                    return new SubscriptionInfoView(
                            event.getSubscriptionId(), event.getName(), event.getDueDate());
                }
                return value;
            }

            @Override
            public SubscriptionInfoView apply(SubscriptionInfoView value, SubscriptionExtendedEvent event) {
                if (event.getSubscriptionId() == subscriptionId) {
                    return new SubscriptionInfoView(
                            value.getSubscriptionId(), value.getSubscriptionName(), event.getNewDueDate());
                }
                return value;
            }

            @Override
            public SubscriptionInfoView apply(SubscriptionInfoView value, SubscriptionCancelledEvent event) {
                if (event.getSubscriptionId() == subscriptionId) {
                    return null;
                }
                return value;
            }
        }.project(null, writeRepository.getAllEvents());
    }

    private long getVisitsCount() {
        return VISIT_STATISTICS.values().stream().reduce(0L, Long::sum);
    }

    @Override
    public VisitReportView getVisitReport() {
        int activeDaysCount = VISIT_STATISTICS.size();
        long visitsCount = getVisitsCount() - CURRENTLY_VISITING.size();
        return new VisitReportView(
                Collections.unmodifiableMap(VISIT_STATISTICS),
                activeDaysCount == 0 ? 0 : 1.0 * getVisitsCount() / activeDaysCount,
                visitsCount == 0 ? Duration.ZERO : visitTimeSum.dividedBy(visitsCount)
        );
    }

    public void startVisit(long subscriptionId, LocalDateTime startTime) {
        LocalDate day = startTime.toLocalDate();
        VISIT_STATISTICS.put(day, VISIT_STATISTICS.getOrDefault(day, 0L) + 1);
        CURRENTLY_VISITING.putIfAbsent(subscriptionId, startTime);
    }

    public void endVisit(long subscriptionId, LocalDateTime endTime) {
        LocalDateTime startTime = CURRENTLY_VISITING.remove(subscriptionId);
        if (startTime != null) {
            visitTimeSum = visitTimeSum.plus(Duration.between(startTime, endTime));
        }
    }
}
