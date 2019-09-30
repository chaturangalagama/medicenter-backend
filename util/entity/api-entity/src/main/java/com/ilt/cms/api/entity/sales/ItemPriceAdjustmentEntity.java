package com.ilt.cms.api.entity.sales;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ItemPriceAdjustmentEntity {

    public enum PaymentType {
        PERCENTAGE, DOLLAR
    }

    private int adjustedValue;
    private PaymentType paymentType;

}
