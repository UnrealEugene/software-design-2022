package ru.akirakozov.sd.cqrs.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.akirakozov.sd.cqrs.model.command.CancelSubscriptionCommand;
import ru.akirakozov.sd.cqrs.model.command.CreateSubscriptionCommand;
import ru.akirakozov.sd.cqrs.model.command.ExtendSubscriptionCommand;
import ru.akirakozov.sd.cqrs.model.query.SubscriptionInfoQuery;
import ru.akirakozov.sd.cqrs.model.view.SubscriptionInfoView;
import ru.akirakozov.sd.cqrs.repository.EventReadRepository;
import ru.akirakozov.sd.cqrs.service.EventService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/manager")
public class ManagerPage {
    private final EventService eventService;
    private final EventReadRepository readRepository;

    @PostMapping("/createSubscription")
    public ResponseEntity<String> createSubscription(@RequestBody CreateSubscriptionCommand command) {
        eventService.send(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/extendSubscription")
    public ResponseEntity<String> extendSubscription(@RequestBody ExtendSubscriptionCommand command) {
        eventService.send(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cancelSubscription")
    public ResponseEntity<String> cancelSubscription(@RequestBody CancelSubscriptionCommand command) {
        eventService.send(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/showSubscriptionInfo")
    public ResponseEntity<SubscriptionInfoView> showSubscriptionInfo(@RequestBody SubscriptionInfoQuery query) {
        return ResponseEntity.ok(readRepository.getSubscriptionInfo(query.getSubscriptionId()));
    }
}
