package ru.unrealeugene.sd.hw2;

import ru.unrealeugene.sd.hw2.api.VkApi;
import ru.unrealeugene.sd.hw2.model.VkPost;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Instant now = Instant.now();
        List<VkPost> posts = new VkApi("https://api.vk.com")
                .getCommunityPosts(
                        163024719,
                        Date.from(now.minus(Duration.ofDays(7))),
                        Date.from(now)
                );
        System.out.println(posts.size());
    }
}
