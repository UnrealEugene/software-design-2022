package ru.akirakozov.sd.clock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class EventStatisticTest {
    private static final long MINUTES_IN_STATISTIC_TIME_INTERVAL =
            EventsStatisticImpl.STATISTIC_TIME_INTERVAL.toMinutes();

    private static final double RPM_EPS = 1e-6;

    private TestClock clock;

    private EventsStatistic eventsStatistic;

    @BeforeEach
    void prepareTest() {
        clock = new TestClock();
        eventsStatistic = new EventsStatisticImpl(clock);
    }

    private void checkEventRpm(String eventName, double expectedRpm) {
        double actualRpmSingle = eventsStatistic.getEventStatisticByName(eventName)
                .orElseThrow(AssertionFailedError::new);
        Assertions.assertEquals(expectedRpm, actualRpmSingle, RPM_EPS);

        double actualRpmAll = eventsStatistic.getAllEventStatistic().get(eventName);
        Assertions.assertEquals(expectedRpm, actualRpmAll, RPM_EPS);
    }

    @Test
    void sameInstantTest() {
        for (int i = 0; i < MINUTES_IN_STATISTIC_TIME_INTERVAL; i++) {
            eventsStatistic.incEvent("1");
            eventsStatistic.incEvent("2");
            eventsStatistic.incEvent("2");
        }

        checkEventRpm("1", 1.0);
        checkEventRpm("2", 2.0);
    }

    @Test
    void outdatedEventsTest() {
        for (int i = 0; i < MINUTES_IN_STATISTIC_TIME_INTERVAL; i++) {
            eventsStatistic.incEvent("1");
            eventsStatistic.incEvent("2");
        }
        clock.rewind(clock.instant().plus(EventsStatisticImpl.STATISTIC_TIME_INTERVAL.multipliedBy(2)));
        for (int i = 0; i < MINUTES_IN_STATISTIC_TIME_INTERVAL; i++) {
            eventsStatistic.incEvent("1");
        }

        checkEventRpm("1", 1.0);
        checkEventRpm("2", 0.0);
    }

    @Test
    void differentInstantsTest() {
        for (int i = 0; i < 2 * MINUTES_IN_STATISTIC_TIME_INTERVAL; i++) {
            eventsStatistic.incEvent("1");
            clock.rewind(clock.instant().plus(1, ChronoUnit.MINUTES));
        }

        checkEventRpm("1", 1.0);

        clock.rewind(clock.instant().plus(EventsStatisticImpl.STATISTIC_TIME_INTERVAL.dividedBy(2)));

        checkEventRpm("1", 0.5);

        clock.rewind(clock.instant().plus(EventsStatisticImpl.STATISTIC_TIME_INTERVAL));

        checkEventRpm("1", 0.0);
    }

    private static class TestClock extends Clock {
        private Instant instant = Instant.EPOCH;

        @Override
        public Instant instant() {
            return instant;
        }

        public void rewind(Instant instant) {
            this.instant = instant;
        }

        @Override
        public ZoneId getZone() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Clock withZone(ZoneId zone) {
            throw new UnsupportedOperationException();
        }
    }
}
