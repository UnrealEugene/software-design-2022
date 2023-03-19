package ru.akirakozov.sd.cqrs.model.query;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SubscriptionInfoQuery {
    private final long subscriptionId;
}
