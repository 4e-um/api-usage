package com.project.rdb.batch.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.rdb.batch.model.entity.BatchExecutionReport;

public interface BatchExecutionReportRepository extends JpaRepository<BatchExecutionReport, Long> {}
