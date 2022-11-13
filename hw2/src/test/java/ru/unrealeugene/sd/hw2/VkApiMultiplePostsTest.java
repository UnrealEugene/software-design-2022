package ru.unrealeugene.sd.hw2;

import org.junit.Assert;
import org.junit.Test;
import org.mockserver.model.Parameter;
import ru.unrealeugene.sd.hw2.api.VkApi;
import ru.unrealeugene.sd.hw2.model.VkPost;
import ru.unrealeugene.sd.hw2.util.PropertyUtil;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

public class VkApiMultiplePostsTest extends VkApiBaseTest {
    private static final long COMMUNITY_ID = 163024719;

    @Test
    public void test() throws IOException {
        try (InputStream stream = VkApiMultiplePostsTest.class.getResourceAsStream("/tests/multiple.json")) {
            byte[] body = Objects.requireNonNull(stream).readAllBytes();
            stubServer.mockApiMethod(body, "wall.get", List.of(
                    Parameter.param("v", PropertyUtil.getProperty("vk.version")),
                    Parameter.param("access_token", PropertyUtil.getProperty("vk.access_token")),
                    Parameter.param("owner_id", Long.toString(-COMMUNITY_ID)),
                    Parameter.param("count", "100")
            ));

            List<VkPost> posts = new VkApi(HOST + ":" + PORT).getCommunityPosts(
                    COMMUNITY_ID,
                    new Date(1668166670000L),
                    new Date(1668321490000L)
            );
            Assert.assertEquals(6, posts.size());
            Assert.assertEquals(List.of(
                    new VkPost(29541, new Date(1668321480000L), "Рейму наказана"),
                    new VkPost(29510, new Date(1668274680000L), ""),
                    new VkPost(29507, new Date(1668249480000L), ""),
                    new VkPost(29499, new Date(1668235080000L), "Фумобухич"),
                    new VkPost(29492, new Date(1668188280000L), "Мгр фумо"),
                    new VkPost(29469, new Date(1668166680000L), "")
            ), posts);
        }
    }
}
