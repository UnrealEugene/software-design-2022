package ru.akirakozov.sd.cqrs.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.akirakozov.sd.cqrs.model.command.EndVisitCommand;
import ru.akirakozov.sd.cqrs.model.command.StartVisitCommand;
import ru.akirakozov.sd.cqrs.service.EventService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/turnstile")
public class TurnstilePage {
    private final EventService eventService;

    @GetMapping("")
    public String index() {
        return "TurnstilePage";
    }

    @PostMapping("/startVisit")
    public ResponseEntity<String> startVisit(@RequestBody StartVisitCommand command) {
        eventService.send(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/endVisit")
    public ResponseEntity<String> endVisit(@RequestBody EndVisitCommand command) {
        eventService.send(command);
        return ResponseEntity.ok().build();
    }
}
