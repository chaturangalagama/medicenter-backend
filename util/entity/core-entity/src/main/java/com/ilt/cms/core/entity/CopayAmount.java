package com.ilt.cms.core.entity;

public class CopayAmount {

    public enum PaymentType {
        PERCENTAGE, DOLLAR
    }

    private int value;
    private PaymentType paymentType = PaymentType.DOLLAR;


    public CopayAmount() {
    }

    public CopayAmount(int value, PaymentType paymentType) {
        this.value = value;
        this.paymentType = paymentType;
    }

    public int getValue() {
        return value;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }


    @Override
    public String toString() {
        return "CopayAmount{" +
                "value=" + value +
                ", paymentType=" + paymentType +
                '}';
    }
}
