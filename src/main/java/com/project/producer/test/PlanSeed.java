package com.project.producer.test;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlanSeed {
    FIVE_G_SIGNATURE("5G 시그니처", -1, PlanUnit.ULTIMATE),
    FIVE_G_STANDARD("5G 스탠다드", 153600, PlanUnit.MONTH),
    FIVE_G_BASIC_PLUS("5G 베이직+", 24576, PlanUnit.MONTH),
    LTE_33("LTE 데이터 33", 1536, PlanUnit.MONTH),
    LTE_DIRECT_45("LTE 다이렉트 45", 5120, PlanUnit.DAY);

    private final String name;
    private final long allowance;
    private final PlanUnit unit;
}
