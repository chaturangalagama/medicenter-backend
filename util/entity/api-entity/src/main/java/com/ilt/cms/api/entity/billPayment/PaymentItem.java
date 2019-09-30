package com.ilt.cms.api.entity.billPayment;

import com.ilt.cms.api.entity.charge.ChargeEntity;
import com.ilt.cms.api.entity.common.UserPaymentOption;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PaymentItem {
    private String itemId;
    private String itemCode;
    private double quantity;
    private ChargeEntity charge;
    private UserPaymentOption discountGiven;
}
