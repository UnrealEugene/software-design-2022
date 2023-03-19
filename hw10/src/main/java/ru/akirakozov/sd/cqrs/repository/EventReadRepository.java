package ru.akirakozov.sd.cqrs.repository;

import ru.akirakozov.sd.cqrs.model.view.SubscriptionInfoView;
import ru.akirakozov.sd.cqrs.model.view.VisitReportView;

public interface EventReadRepository {
    SubscriptionInfoView getSubscriptionInfo(long subscriptionId);
    VisitReportView getVisitReport();
}
