package com.ilt.cms.api.entity.billPayment;

import com.ilt.cms.api.entity.charge.ChargeEntity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ItemCharge {
    private String itemId;
    private ChargeEntity charge;
}
