package ru.akirakozov.sd.clock;

import java.util.Map;
import java.util.OptionalDouble;

public interface EventsStatistic {
    void incEvent(String name);
    OptionalDouble getEventStatisticByName(String name);
    Map<String, Double> getAllEventStatistic();
    void printStatistic();
}
