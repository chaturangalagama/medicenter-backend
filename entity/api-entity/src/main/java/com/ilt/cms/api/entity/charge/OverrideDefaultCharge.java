package com.ilt.cms.api.entity.charge;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class OverrideDefaultCharge {
    private Condition condition;
    private ChargeEntity charge;


    public boolean doesConstraintMatch(Class<? extends Condition> type, Object obj) {
        return condition.getClass().equals(type) && this.condition.match(obj);
    }

}
