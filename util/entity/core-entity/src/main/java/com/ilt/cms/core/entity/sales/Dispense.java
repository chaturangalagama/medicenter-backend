package com.ilt.cms.core.entity.sales;

import java.time.LocalDateTime;
import java.util.List;

public class Dispense {

    private LocalDateTime dispenseTime;
    private List<DispenseItem> dispenseItems;

    public Dispense() {
    }

    public Dispense(LocalDateTime dispenseTime, List<DispenseItem> dispenseItems) {
        this.dispenseTime = dispenseTime;
        this.dispenseItems = dispenseItems;
    }

    public static class DispenseItem {
        private String salesItemIdRef;
        private int dispenseQty;

        public DispenseItem() {
        }

        public DispenseItem(String salesItemIdRef, int dispenseQty) {
            this.salesItemIdRef = salesItemIdRef;
            this.dispenseQty = dispenseQty;
        }

        public String getSalesItemIdRef() {
            return salesItemIdRef;
        }

        public int getDispenseQty() {
            return dispenseQty;
        }
    }

    public LocalDateTime getDispenseTime() {
        return dispenseTime;
    }

    public List<DispenseItem> getDispenseItems() {
        return dispenseItems;
    }

    @Override
    public String toString() {
        return "Dispense{" +
                "dispenseTime=" + dispenseTime +
                ", dispenseItems=" + dispenseItems +
                '}';
    }
}
