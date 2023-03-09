package ru.akirakozov.sd.actors.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.SupervisorStrategy;
import akka.actor.typed.javadsl.*;
import ru.akirakozov.sd.actors.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class MasterActor extends AbstractBehavior<MasterActorCommand> {
    public static final List<String> SEARCH_ENGINES = List.of(
            "google",
            "yandex",
            "bing"
    );

    private final ActorRef<SearchResponse> parent;
    private final List<SearchEngineResponse> searchEngineResponses;

    private int responseCount;

    private MasterActor(ActorContext<MasterActorCommand> context, ActorRef<SearchResponse> parent) {
        super(context);
        this.parent = parent;
        this.searchEngineResponses = new ArrayList<>();
        this.responseCount = 0;
    }

    public static Behavior<MasterActorCommand> create(ActorRef<SearchResponse> parent) {
        return Behaviors.setup(context -> new MasterActor(context, parent));
    }

    @Override
    public Receive<MasterActorCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(SearchEngineResponse.class, this::onReceiveSearchEngineResponse)
                .onMessage(SearchEngineResponseTimeout.class, this::onReceiveSearchEngineResponseTimeout)
                .onMessage(SearchRequest.class, this::onReceiveSearchQuery)
                .build();
    }

    private Behavior<MasterActorCommand> onReceiveFromChildCommon() {
        if (responseCount == SEARCH_ENGINES.size()) {
            parent.tell(new SearchResponse(searchEngineResponses));
            return Behaviors.stopped();
        }
        return Behaviors.same();
    }

    private Behavior<MasterActorCommand> onReceiveSearchEngineResponse(SearchEngineResponse response) {
        searchEngineResponses.add(response);
        responseCount += 1;
        return onReceiveFromChildCommon();
    }

    private Behavior<MasterActorCommand> onReceiveSearchEngineResponseTimeout(SearchEngineResponseTimeout response) {
        getContext().getLog().warn("Request to " + response.getEngine() + " has timed out!");
        responseCount += 1;
        return onReceiveFromChildCommon();
    }

    private Behavior<MasterActorCommand> onReceiveSearchQuery(SearchRequest searchRequest) {
        for (String searchEngine : SEARCH_ENGINES) {
            ActorRef<ChildActorCommand> childActor =
                    getContext().spawn(ChildActor.create(getContext().getSelf()), searchEngine + "ChildActor");
            getContext().ask(
                    MasterActorCommand.class,
                    childActor,
                    searchRequest.getTimeout(),
                    ref -> new SearchEngineRequest(searchEngine, searchRequest),
                    (response, throwable) ->
                            response != null ? response : new SearchEngineResponseTimeout(searchEngine)
            );
        }
        return Behaviors.same();
    }
}
