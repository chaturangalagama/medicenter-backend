package com.ilt.cms.api.entity.medicalTest;

import com.ilt.cms.api.entity.charge.ChargeEntity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MedicalTestSchemeEntity {
    private String medicalTestId;
    private ChargeEntity price;
}
