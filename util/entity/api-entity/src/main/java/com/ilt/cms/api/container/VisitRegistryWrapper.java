package com.ilt.cms.api.container;

import com.ilt.cms.api.entity.patientVisitRegistry.VisitRegistryEntity;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VisitRegistryWrapper {

    private VisitRegistryEntity registryEntity;
    private Boolean singleVisitCase;

}
