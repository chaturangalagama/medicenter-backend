package com.ilt.cms.api.entity.billPayment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class CopayAmount {
    public enum PaymentType {
        PERCENTAGE, DOLLAR
    }

    private int value;
    private PaymentType paymentType = PaymentType.DOLLAR;
}
