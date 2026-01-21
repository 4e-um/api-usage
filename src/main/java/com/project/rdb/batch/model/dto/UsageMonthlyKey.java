package com.project.rdb.batch.model.dto;

import java.util.Objects;

public record UsageMonthlyKey(Long subId, String period) {

    @Override
    public boolean equals(Object oj) {
        if (this == oj) {
            return true;
        }
        if (!(oj instanceof UsageMonthlyKey)) {
            return false;
        }
        UsageMonthlyKey that = (UsageMonthlyKey) oj;
        return Objects.equals(subId, that.subId) && Objects.equals(period, that.period);
    }
}
