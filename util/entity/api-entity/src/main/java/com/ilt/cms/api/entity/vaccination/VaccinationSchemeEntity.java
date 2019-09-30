package com.ilt.cms.api.entity.vaccination;

import com.ilt.cms.api.entity.charge.ChargeEntity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class VaccinationSchemeEntity {
    private String doseId;
    private ChargeEntity price;
}
