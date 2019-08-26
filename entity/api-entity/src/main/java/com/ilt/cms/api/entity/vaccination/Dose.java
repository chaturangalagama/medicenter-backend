package com.ilt.cms.api.entity.vaccination;

import com.ilt.cms.api.entity.charge.ChargeEntity;
import com.ilt.cms.api.entity.common.UserPaymentOption;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Dose {
    private String doseId;
    private String name;
    private String code;
    private String description;
    private ChargeEntity price;
    private UserPaymentOption priceAdjustment;
    private int nextDoseRecommendedGap;
}
