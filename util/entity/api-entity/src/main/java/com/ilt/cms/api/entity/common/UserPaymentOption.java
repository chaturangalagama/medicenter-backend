package com.ilt.cms.api.entity.common;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserPaymentOption {
    public enum PaymentType {
        PERCENTAGE, DOLLAR
    }

    private int decreaseValue;
    private int increaseValue;
    private PaymentType paymentType;
    private String remark;
}
