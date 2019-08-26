package com.ilt.cms.api.entity.vaccination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class AssociatedCoverageVaccinationEntity {
    private String id;
    private String medicalCoverageId;
    private String coveragePlanId;
    private List<VaccinationSchemeEntity> modifiedVaccinationScheme;

    private List<VaccinationSchemeEntity> excludedVaccinationScheme;

    public AssociatedCoverageVaccinationEntity() {
        modifiedVaccinationScheme = new ArrayList<>();
        excludedVaccinationScheme = new ArrayList<>();
    }

    public AssociatedCoverageVaccinationEntity(String medicalCoverageId, String coveragePlanId) {
        this();
        this.medicalCoverageId = medicalCoverageId;
        this.coveragePlanId = coveragePlanId;
    }

    public void clearFieldsForPersistence() {
        id = null;
    }


    public void addModifiedMedicalTestSchemeEntity(VaccinationSchemeEntity medicalTestScheme) {
        modifiedVaccinationScheme.add(medicalTestScheme);
    }

    public void addExcludedMedicalTestSchemeEntity(VaccinationSchemeEntity medicalTestScheme) {
        excludedVaccinationScheme.add(medicalTestScheme);

    }

}
