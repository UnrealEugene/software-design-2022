package ru.akirakozov.sd.cqrs;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.akirakozov.sd.cqrs.controller.ManagerPage;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SpringBootTest
public class SmokeTest {
    private final ManagerPage controller;

    @Test
    public void contextLoads() {
        Assertions.assertThat(controller).isNotNull();
    }
}
