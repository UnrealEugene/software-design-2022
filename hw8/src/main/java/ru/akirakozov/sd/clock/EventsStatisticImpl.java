package ru.akirakozov.sd.clock;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.*;
import java.util.stream.Collectors;

public class EventsStatisticImpl implements EventsStatistic {
    static final Duration STATISTIC_TIME_INTERVAL = Duration.of(1, ChronoUnit.HOURS);

    private final Clock clock;

    private final Queue<Event> eventQueue;

    private final Map<String, Long> eventCountByName;

    public EventsStatisticImpl(Clock clock) {
        this.clock = clock;
        this.eventQueue = new LinkedList<>();
        this.eventCountByName = new HashMap<>();
    }

    @Override
    public void incEvent(String name) {
        eventCountByName.merge(name, 1L, Long::sum);
        eventQueue.add(new Event(name, clock.instant()));
    }

    private void validateEventQueue() {
        Instant threshold = clock.instant().minus(STATISTIC_TIME_INTERVAL);
        while (!eventQueue.isEmpty() && eventQueue.peek().getTimestamp().isBefore(threshold)) {
            Event event = eventQueue.remove();
            eventCountByName.computeIfPresent(event.getName(), (key, count) -> count - 1);
        }
    }

    private double getRpmFromCount(long count) {
        return 1.0 * count / STATISTIC_TIME_INTERVAL.toMinutes();
    }

    @Override
    public OptionalDouble getEventStatisticByName(String name) {
        validateEventQueue();
        if (!eventCountByName.containsKey(name)) {
            return OptionalDouble.empty();
        }
        return OptionalDouble.of(getRpmFromCount(eventCountByName.get(name)));
    }

    @Override
    public Map<String, Double> getAllEventStatistic() {
        validateEventQueue();
        return eventCountByName.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> getRpmFromCount(e.getValue())));
    }

    @Override
    public void printStatistic() {
        getAllEventStatistic().forEach((eventName, rpm) ->
                System.out.println("Event " + eventName + ", rpm: " + rpm));
    }

    private static class Event {
        private final String name;
        private final Instant timestamp;

        private Event(String name, Instant timestamp) {
            this.name = name;
            this.timestamp = timestamp;
        }

        public String getName() {
            return name;
        }

        public Instant getTimestamp() {
            return timestamp;
        }
    }
}
