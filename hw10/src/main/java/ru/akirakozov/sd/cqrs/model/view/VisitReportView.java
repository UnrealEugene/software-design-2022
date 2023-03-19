package ru.akirakozov.sd.cqrs.model.view;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class VisitReportView {
    private final Map<LocalDate, Long> visitStatistics;
    private final double averageVisitsPerDay;
    private final Duration averageVisitTime;
}
