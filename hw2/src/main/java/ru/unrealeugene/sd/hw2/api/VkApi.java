package ru.unrealeugene.sd.hw2.api;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import ru.unrealeugene.sd.hw2.model.VkPost;
import ru.unrealeugene.sd.hw2.util.PropertyUtil;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class VkApi {
    private static final String API_ACCESS_TOKEN = PropertyUtil.getProperty("vk.access_token");
    private static final String API_VERSION = PropertyUtil.getProperty("vk.version");
    private static final int POST_COUNT = 100;

    private final String apiUrl;
    private final Client client;

    public VkApi(String url) {
        this.apiUrl = url;
        this.client = ClientBuilder.newClient();
    }

    public List<VkPost> getCommunityPosts(long communityId, Date fromDate, Date toDate) {
        String body = client.target(apiUrl)
                .path("method/wall.get")
                .queryParam("v", API_VERSION)
                .queryParam("access_token", API_ACCESS_TOKEN)
                .queryParam("owner_id", Long.toString(-communityId))
                .queryParam("count", POST_COUNT)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(String.class);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new UnixTimestampAdapter())
                .create();
        JsonArray jsonArray = gson.fromJson(body, JsonObject.class)
                .getAsJsonObject("response")
                .getAsJsonArray("items");
        return Arrays.stream(gson.fromJson(jsonArray, VkPost[].class))
                .filter(post -> post.getDate().after(fromDate) && post.getDate().before(toDate))
                .collect(Collectors.toList());
    }

    public static class UnixTimestampAdapter extends TypeAdapter<Date> {
        @Override
        public void write(JsonWriter out, Date value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            out.value(value.getTime() / 1000);
        }

        @Override
        public Date read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return new Date(in.nextLong() * 1000);
        }
    }
}
