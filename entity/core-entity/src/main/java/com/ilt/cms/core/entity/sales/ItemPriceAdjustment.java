package com.ilt.cms.core.entity.sales;

import com.lippo.cms.util.Calculations;

public class ItemPriceAdjustment {

    public enum PaymentType {
        PERCENTAGE, DOLLAR
    }

    private int adjustedValue;
    private PaymentType paymentType = PaymentType.DOLLAR;
    private String remark;

    public ItemPriceAdjustment() {
    }

    public ItemPriceAdjustment(int adjustedValue, PaymentType paymentType, String remark) {
        this.adjustedValue = adjustedValue;
        this.paymentType = paymentType;
        this.remark = remark;
    }

    public int calculateAdjustedPrice(int originalPrice) {
        if (paymentType == PaymentType.DOLLAR) {
            return originalPrice + adjustedValue;
        } else {
            if (adjustedValue < 0) {
                int percentage = Calculations.calculatePercentage(originalPrice, Math.abs(adjustedValue));
                return originalPrice - percentage;
            } else {
                int percentage = Calculations.calculatePercentage(originalPrice, adjustedValue);
                return originalPrice + percentage;
            }
        }
    }

    public ItemPriceAdjustment(int adjustedValue) {
        this.adjustedValue = adjustedValue;
    }

    public int getAdjustedValue() {
        return adjustedValue;
    }

    public void setAdjustedValue(int adjustedValue) {
        this.adjustedValue = adjustedValue;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "ItemPriceAdjustment{" +
                "adjustedValue=" + adjustedValue +
                ", paymentType=" + paymentType +
                ", remark='" + remark + '\'' +
                '}';
    }
}
