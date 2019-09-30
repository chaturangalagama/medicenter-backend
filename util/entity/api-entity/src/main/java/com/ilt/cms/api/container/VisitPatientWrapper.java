package com.ilt.cms.api.container;

import com.ilt.cms.api.entity.common.UserId;
import com.ilt.cms.api.entity.patientVisitRegistry.VisitRegistryEntity;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VisitPatientWrapper {

    private VisitRegistryEntity registryEntity;
    private String patientName;
    private UserId userId;

}
