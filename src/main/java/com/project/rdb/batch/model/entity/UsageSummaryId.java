package com.project.rdb.batch.model.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UsageSummaryId implements Serializable {

    @Column(name = "sub_id")
    private Long subId;

    @Column(length = 6)
    private String period;

    @Override
    public boolean equals(Object oj) {
        if (this == oj) {
            return true;
        }
        if (!(oj instanceof UsageSummaryId that)) {
            return false;
        }
        return subId.equals(that.subId) && period.equals(that.period);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subId, period);
    }
}
