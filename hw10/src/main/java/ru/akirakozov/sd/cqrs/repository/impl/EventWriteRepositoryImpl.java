package ru.akirakozov.sd.cqrs.repository.impl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.akirakozov.sd.cqrs.model.AbstractProjector;
import ru.akirakozov.sd.cqrs.model.dto.EventDto;
import ru.akirakozov.sd.cqrs.model.event.Event;
import ru.akirakozov.sd.cqrs.model.event.SubscriptionCancelledEvent;
import ru.akirakozov.sd.cqrs.model.event.SubscriptionCreatedEvent;
import ru.akirakozov.sd.cqrs.model.event.SubscriptionExtendedEvent;
import ru.akirakozov.sd.cqrs.repository.EventWriteRepository;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Repository
public class EventWriteRepositoryImpl implements EventWriteRepository {
    private final JdbcTemplate jdbcTemplate;

    private void addRawEvent(EventDto eventDto) {
        jdbcTemplate.update("insert into Event values (?, ?, ?)",
                eventDto.getId(), eventDto.getCreationTime(), eventDto.getJsonData());
    }

    @Override
    public void addEvent(Event event) {
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(event);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        jsonObject.remove("id");
        jsonObject.remove("creationTime");
        jsonObject.addProperty("type", event.getClass().getCanonicalName());

        String json = gson.toJson(jsonElement);
        EventDto eventDto = new EventDto(event.getId(), event.getCreationTime(), json);
        addRawEvent(eventDto);
    }

    private List<EventDto> getAllRawEvents() {
        return jdbcTemplate.query(
                "select * from Event order by creationTime",
                (row, rowNum) -> new EventDto(
                        row.getObject("id", UUID.class),
                        row.getObject("creationTime", LocalDateTime.class),
                        row.getString("jsonData")
                )
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Event> getAllEvents() {
        try {
            List<EventDto> rawEvents = getAllRawEvents();
            List<Event> events = new ArrayList<>();
            Gson gson = new Gson();
            for (EventDto eventDto : rawEvents) {
                JsonObject jsonObject = gson.fromJson(eventDto.getJsonData(), JsonObject.class);
                Class<? extends Event> clazz = (Class<? extends Event>) Class.forName(jsonObject.get("type").getAsString());
                jsonObject.add("id", gson.toJsonTree(eventDto.getId()));
                jsonObject.add("creationTime", gson.toJsonTree(eventDto.getCreationTime()));
                events.add(gson.fromJson(jsonObject, clazz));
            }
            return events;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long getFreeSubscriptionId() {
        Set<Long> usedIds = new AbstractProjector<Set<Long>>() {
            @Override
            public Set<Long> apply(Set<Long> set, SubscriptionCreatedEvent event) {
                set.add(event.getSubscriptionId());
                return set;
            }
        }.project(new HashSet<>(), getAllEvents());
        long id = 1;
        while (usedIds.contains(id)) {
            id++;
        }
        return id;
    }

    @Override
    public String getPasswordHash(long subscriptionId) {
        return new AbstractProjector<String>() {
            @Override
            public String apply(String value, SubscriptionCreatedEvent event) {
                if (event.getSubscriptionId() == subscriptionId) {
                    return event.getPasswordHash();
                }
                return value;
            }
        }.project(null, getAllEvents());
    }

    @Override
    public boolean canStartVisit(long subscriptionId, LocalDateTime now) {
        return new AbstractProjector<Boolean>() {
            @Override
            public Boolean apply(Boolean value, SubscriptionCreatedEvent event) {
                if (event.getSubscriptionId() == subscriptionId) {
                    return now.isBefore(event.getDueDate().atStartOfDay());
                }
                return value;
            }

            @Override
            public Boolean apply(Boolean value, SubscriptionExtendedEvent event) {
                if (event.getSubscriptionId() == subscriptionId) {
                    return now.isBefore(event.getNewDueDate().atStartOfDay());
                }
                return value;
            }

            @Override
            public Boolean apply(Boolean value, SubscriptionCancelledEvent event) {
                if (event.getSubscriptionId() == subscriptionId) {
                    return false;
                }
                return value;
            }
        }.project(false, getAllEvents());
    }
}
