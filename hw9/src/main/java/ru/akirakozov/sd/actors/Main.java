package ru.akirakozov.sd.actors;


import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import ru.akirakozov.sd.actors.actor.MasterActor;
import ru.akirakozov.sd.actors.model.MasterActorCommand;
import ru.akirakozov.sd.actors.model.SearchEngineResponse;
import ru.akirakozov.sd.actors.model.SearchRequest;
import ru.akirakozov.sd.actors.model.SearchResponse;

import java.time.Duration;
import java.util.function.Consumer;

public class Main {
    private static void printSearchResponse(SearchResponse response) {
        if (response.getEngineResponses().isEmpty()) {
            System.out.println("No results");
        }
        for (SearchEngineResponse searchEngineResponse : response.getEngineResponses()) {
            System.out.println("Search engine: " + searchEngineResponse.getEngine());
            for (SearchEngineResponse.Item item : searchEngineResponse.getResults()) {
                System.out.println(" " + item.getLink());
                System.out.println("  " + item.getTitle());
            }
        }
    }

    public static Behavior<SearchResponse> create(String query, int resultCount, Duration timeout,
                                                  Consumer<SearchResponse> callback) {
        return Behaviors.setup(context -> {
            ActorRef<MasterActorCommand> masterActor =
                    context.spawn(MasterActor.create(context.getSelf()), "masterActor");
            masterActor.tell(new SearchRequest(query, resultCount, timeout));
            return Behaviors.receive(SearchResponse.class)
                    .onMessage(SearchResponse.class, response -> {
                        callback.accept(response);
                        return Behaviors.stopped();
                    })
                    .build();
        });
    }

    public static void main(String[] args) {
        ActorSystem.create(
                Main.create(
                        "coffee",
                        5,
                        Duration.ofSeconds(10),
                        Main::printSearchResponse
                ),
                "search"
        );
    }
}