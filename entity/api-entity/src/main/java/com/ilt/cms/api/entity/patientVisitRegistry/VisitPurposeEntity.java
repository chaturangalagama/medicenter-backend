package com.ilt.cms.api.entity.patientVisitRegistry;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class VisitPurposeEntity {
    private String id;
    private String name;
    private boolean consultationRequired;
}

