package com.ilt.cms.api.entity.medicalService;

import com.ilt.cms.api.entity.charge.ChargeEntity;
import com.ilt.cms.api.entity.common.UserPaymentOption;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MedicalServiceItem {
    private String id;
    private String name;
    private String code;
    private String description;
    private ChargeEntity chargeAmount;
    private UserPaymentOption priceAdjustment;
}
