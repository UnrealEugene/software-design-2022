package ru.akirakozov.sd.cqrs.model.view;

import lombok.*;

import java.time.LocalDate;

@Data
public class SubscriptionInfoView {
    private final long subscriptionId;
    private final String subscriptionName;
    private final LocalDate subscriptionDueDate;
}
