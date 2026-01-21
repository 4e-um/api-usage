package com.project.rdb.batch.model.dto;

import java.util.Objects;

public record UsageDailyKey(Long subId, String usageDate) {
    @Override
    public boolean equals(Object oj) {
        if (this == oj) {
            return true;
        }
        if (!(oj instanceof UsageDailyKey)) {
            return false;
        }
        UsageDailyKey that = (UsageDailyKey) oj;
        return Objects.equals(subId, that.subId) && Objects.equals(usageDate, that.usageDate);
    }
}
