package ru.akirakozov.sd.cqrs.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.akirakozov.sd.cqrs.model.view.VisitReportView;
import ru.akirakozov.sd.cqrs.repository.EventReadRepository;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/report")
public class ReportPage {
    private final EventReadRepository readRepository;

    @GetMapping("")
    public ResponseEntity<VisitReportView> getVisitReport() {
        return ResponseEntity.ok(readRepository.getVisitReport());
    }
}
