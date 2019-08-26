package com.ilt.cms.api.entity.billPayment;

import com.ilt.cms.api.entity.charge.ChargeEntity;
import com.ilt.cms.api.entity.coverage.MedicalCoverageEntity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ItemCharge {
    private String itemId;
    private ChargeEntity charge;
    private MedicalCoverageEntity medicalCoverage;
}
