package ru.akirakozov.sd.actors.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ru.akirakozov.sd.actors.model.ChildActorCommand;
import ru.akirakozov.sd.actors.model.MasterActorCommand;
import ru.akirakozov.sd.actors.model.SearchEngineRequest;
import ru.akirakozov.sd.actors.model.SearchEngineResponse;
import ru.akirakozov.sd.actors.util.PropertyUtil;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

public class ChildActor extends AbstractBehavior<ChildActorCommand> {
    private static final String API_URL = PropertyUtil.getProperty("serpapi.url");
    private static final String API_KEY = PropertyUtil.getProperty("serpapi.private_key");

    private final ActorRef<MasterActorCommand> parent;

    private ChildActor(ActorContext<ChildActorCommand> context, ActorRef<MasterActorCommand> parent) {
        super(context);
        this.parent = parent;
    }

    public static Behavior<ChildActorCommand> create(ActorRef<MasterActorCommand> parent) {
        return Behaviors.setup(context -> new ChildActor(context, parent));
    }

    @Override
    public Receive<ChildActorCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(SearchEngineRequest.class, this::onReceiveSearchEngineRequest)
                .build();
    }

    private Behavior<ChildActorCommand> onReceiveSearchEngineRequest(SearchEngineRequest searchEngineRequest) {
        String body = new String(ClientBuilder.newClient()
                .target(API_URL)
                .queryParam("engine", searchEngineRequest.getEngine())
                .queryParam("q", searchEngineRequest.getSearchRequest().getQuery())
                .queryParam("text", searchEngineRequest.getSearchRequest().getQuery())
                .queryParam("api_key", API_KEY)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(byte[].class), Charset.forName("Windows-1251"));
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(body, JsonObject.class)
                .getAsJsonArray("organic_results");
        List<SearchEngineResponse.Item> itemList =
                Arrays.stream(gson.fromJson(jsonArray, SearchEngineResponse.Item[].class))
                        .limit(searchEngineRequest.getSearchRequest().getResultCount())
                        .toList();

        parent.tell(new SearchEngineResponse(searchEngineRequest.getEngine(), itemList));
        return Behaviors.stopped();
    }
}
