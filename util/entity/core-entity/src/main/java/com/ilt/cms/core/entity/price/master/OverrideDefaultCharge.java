package com.ilt.cms.core.entity.price.master;

import com.ilt.cms.core.entity.item.SellingPrice;

public class OverrideDefaultCharge {

    private Condition condition;
    private SellingPrice sellingPrice;


    public OverrideDefaultCharge() {
    }

    public OverrideDefaultCharge(Condition condition, SellingPrice sellingPrice) {
        this.condition = condition;
        this.sellingPrice = sellingPrice;
    }

    public boolean doesConstraintMatch(Class<? extends Condition> type, Object obj) {
        return condition.getClass().equals(type) && this.condition.match(obj);
    }

    public boolean forcePriority() {
        return condition.forcePriority();
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public SellingPrice getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(SellingPrice sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    @Override
    public String toString() {
        return "OverrideDefaultCharge{" +
                "condition=" + condition +
                ", sellingPrice=" + sellingPrice +
                '}';
    }
}
