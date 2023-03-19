package ru.akirakozov.sd.cqrs;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.akirakozov.sd.cqrs.model.command.*;
import ru.akirakozov.sd.cqrs.model.query.SubscriptionInfoQuery;
import ru.akirakozov.sd.cqrs.model.view.SubscriptionInfoView;
import ru.akirakozov.sd.cqrs.model.view.VisitReportView;

import java.time.LocalDate;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Hw10ApplicationTests {
    private final TestRestTemplate restTemplate;
    private final JdbcTemplate jdbcTemplate;

    @AfterEach
    public void after() {
        jdbcTemplate.execute("delete from Event where true");
    }

    @Test
    public void testCreateSubscription() {
        CreateSubscriptionCommand command =
                new CreateSubscriptionCommand("Eugene", "12345", LocalDate.now().plusDays(3));
        createSubscription(command);

        SubscriptionInfoView showSubscriptionInfo = showSubscriptionInfo(new SubscriptionInfoQuery(1));
        Assertions.assertEquals(
                new SubscriptionInfoView(1, command.getName(), command.getDueDate()),
                showSubscriptionInfo
        );
    }

    @Test
    public void testExtendSubscription() {
        LocalDate now = LocalDate.now();

        CreateSubscriptionCommand createCommand =
                new CreateSubscriptionCommand("Eugene", "12345", now.plusDays(1));
        createSubscription(createCommand);

        ExtendSubscriptionCommand extendCommand =
                new ExtendSubscriptionCommand(1, now.plusDays(2));
        extendSubscription(extendCommand);

        Assertions.assertEquals(
                new SubscriptionInfoView(1, createCommand.getName(), extendCommand.getNewDueDate()),
                showSubscriptionInfo(new SubscriptionInfoQuery(1))
        );
    }

    @Test
    public void testCancelSubscription() {
        CreateSubscriptionCommand createCommand1 =
                new CreateSubscriptionCommand("Eugene", "12345", LocalDate.now().plusDays(1));
        createSubscription(createCommand1);

        CancelSubscriptionCommand cancelCommand = new CancelSubscriptionCommand(1);
        cancelSubscription(cancelCommand);

        CreateSubscriptionCommand createCommand2 =
                new CreateSubscriptionCommand("Ivan", "123456", LocalDate.now().plusDays(3));
        createSubscription(createCommand2);

        Assertions.assertNull(showSubscriptionInfo(new SubscriptionInfoQuery(1)));
    }

    @Test
    public void testSequentialCreateSubscription() {
        LocalDate now = LocalDate.now();

        CreateSubscriptionCommand createCommand1 =
                new CreateSubscriptionCommand("Eugene", "12345", now.plusDays(1));
        createSubscription(createCommand1);

        CreateSubscriptionCommand createCommand2 =
                new CreateSubscriptionCommand("Ivan", "123456", now.plusDays(3));
        createSubscription(createCommand2);

        Assertions.assertEquals(
                new SubscriptionInfoView(1, createCommand1.getName(), createCommand1.getDueDate()),
                showSubscriptionInfo(new SubscriptionInfoQuery(1))
        );

        Assertions.assertEquals(
                new SubscriptionInfoView(2, createCommand2.getName(), createCommand2.getDueDate()),
                showSubscriptionInfo(new SubscriptionInfoQuery(2))
        );
    }

    @Test
    public void testVisit() {
        CreateSubscriptionCommand createCommand =
                new CreateSubscriptionCommand("Eugene", "12345", LocalDate.now().plusDays(1));
        createSubscription(createCommand);

        startVisit(new StartVisitCommand(1, createCommand.getPassword()));
        endVisit(new EndVisitCommand(1));

//        VisitReportView visitReport = showSubscriptionInfo();
//        Map<LocalDate, Long> visitStatistics = visitReport.getVisitStatistics();
//        Assertions.assertEquals(1, visitStatistics.size());
//        Assertions.assertEquals(1, visitStatistics.values().iterator().next());
//        Assertions.assertEquals(1.0, visitReport.getAverageVisitsPerDay());
    }

    private void postWithNullResponse(String url, Object entity) {
        ResponseEntity<String> createSubscriptionResponse = restTemplate.postForEntity(url, entity, String.class);
        Assertions.assertEquals(createSubscriptionResponse.getStatusCode(), HttpStatus.OK);
    }

    private void createSubscription(CreateSubscriptionCommand command) {
        postWithNullResponse("/api/manager/createSubscription", command);
    }

    private void extendSubscription(ExtendSubscriptionCommand command) {
        postWithNullResponse("/api/manager/extendSubscription", command);
    }

    private void cancelSubscription(CancelSubscriptionCommand command) {
        postWithNullResponse("/api/manager/cancelSubscription", command);
    }

    private void startVisit(StartVisitCommand command) {
        postWithNullResponse("/api/turnstile/startVisit", command);
    }

    private void endVisit(EndVisitCommand command) {
        postWithNullResponse("/api/turnstile/endVisit", command);
    }

    private SubscriptionInfoView showSubscriptionInfo(SubscriptionInfoQuery query) {
        return restTemplate.postForEntity("/api/manager/showSubscriptionInfo", query, SubscriptionInfoView.class).getBody();
    }

    private VisitReportView showSubscriptionInfo() {
        return restTemplate.getForEntity("/api/report", VisitReportView.class).getBody();
    }
}
