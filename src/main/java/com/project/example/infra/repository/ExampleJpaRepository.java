package com.project.example.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.example.infra.entity.ExampleEntity;

public interface ExampleJpaRepository extends JpaRepository<ExampleEntity, Long> {}
