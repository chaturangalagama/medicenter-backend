package com.ilt.cms.core.entity;

public class UserPaymentOption {

    public enum PaymentType {
        PERCENTAGE, DOLLAR
    }

    private int decreaseValue;
    private int increaseValue;
    private PaymentType paymentType;
    private String remark;

    /**
     * Just to sugarcoat of using new keyword
     *
     * @param decreaseValue
     * @param increaseValue
     * @param paymentType
     * @param remark
     * @return
     */
    public static UserPaymentOption newInstance(int decreaseValue, int increaseValue, PaymentType paymentType,
                                                String remark) {
        return new UserPaymentOption(decreaseValue, increaseValue, paymentType, remark);
    }

    public UserPaymentOption(int decreaseValue, int increaseValue, PaymentType paymentType) {
        this.decreaseValue = decreaseValue;
        this.increaseValue = increaseValue;
        this.paymentType = paymentType;
    }

    public UserPaymentOption(int decreaseValue, int increaseValue, PaymentType paymentType, String remark) {
        this.decreaseValue = decreaseValue;
        this.increaseValue = increaseValue;
        this.paymentType = paymentType;
        this.remark = remark;
    }


    public UserPaymentOption() {
    }

    public int getDecreaseValue() {
        return decreaseValue;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }


    public String getRemark() {
        return remark;
    }

    public int getIncreaseValue() {
        return increaseValue;
    }

    @Override
    public String toString() {
        return "UserPaymentOption{" +
                "decreaseValue=" + decreaseValue +
                ", increaseValue=" + increaseValue +
                ", paymentType=" + paymentType +
                ", remark='" + remark + '\'' +
                '}';
    }
}
