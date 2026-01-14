package com.project.example.controller;

import com.project.example.controller.dto.SaveExampleRequest;
import com.project.example.infra.entity.ExampleEntity;
import com.project.example.service.ExampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/example")
@RequiredArgsConstructor
public class ExampleController {

  private final ExampleService exampleService;

  @GetMapping("/{exampleId}")
  public ResponseEntity<ExampleEntity> find(@PathVariable Long exampleId) {
    return ResponseEntity.ok(exampleService.find(exampleId));
  }

  @PostMapping
  public void save(@RequestBody SaveExampleRequest request) {
    exampleService.save(request);
  }
}
