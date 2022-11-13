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

public class VkApiSinglePostTest extends VkApiBaseTest {
    private static final long COMMUNITY_ID = 163024719;

    @Test
    public void test() throws IOException {
        try (InputStream stream = VkApiSinglePostTest.class.getResourceAsStream("/tests/single.json")) {
            byte[] body = Objects.requireNonNull(stream).readAllBytes();
            stubServer.mockApiMethod(body, "wall.get", List.of(
                    Parameter.param("v", PropertyUtil.getProperty("vk.version")),
                    Parameter.param("access_token", PropertyUtil.getProperty("vk.access_token")),
                    Parameter.param("owner_id", Long.toString(-COMMUNITY_ID)),
                    Parameter.optionalParam("count")
            ));

            List<VkPost> posts = new VkApi(HOST + ":" + PORT).getCommunityPosts(
                    COMMUNITY_ID,
                    new Date(Long.MIN_VALUE),
                    new Date(Long.MAX_VALUE)
            );
            Assert.assertEquals(1, posts.size());
            Assert.assertEquals(new VkPost(
                    18828,
                    new Date(1644443671000L),
                    "‼ ‼ новые правила группы: ‼ ‼"
            ), posts.get(0));
        }
    }
}
