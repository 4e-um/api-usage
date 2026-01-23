package com.project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.rdb.batch.orchestrator.UsageOrchestrator;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/batch")
@RequiredArgsConstructor
public class BatchController {

    private final UsageOrchestrator usageOrchestrator;

    @PostMapping("/usage/run")
    public ResponseEntity<String> runUsagePipeline() {
        try {
            usageOrchestrator.run();
            return ResponseEntity.ok("✅ usage batch pipeline started");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("❌ batch execution failed: " + e.getMessage());
        }
    }
}
