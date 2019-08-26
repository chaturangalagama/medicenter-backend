package com.ilt.cms.api.payload;

import com.ilt.cms.api.entity.coverage.CoveragePlanEntity;
import com.ilt.cms.api.entity.coverage.MedicalServiceSchemeEntity;
import com.ilt.cms.api.entity.medicalService.MedicalServiceEntity;
import com.ilt.cms.api.entity.medicalTest.MedicalTestEntity;
import com.ilt.cms.api.entity.medicalTest.MedicalTestSchemeEntity;
import com.ilt.cms.api.entity.vaccination.VaccinationEntity;
import com.ilt.cms.api.entity.vaccination.VaccinationSchemeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PlanItemAssociation {
    private CoveragePlanEntity coveragePlan;

    private String drugAssociationId;
    private String medicalTestAssociationId;
    private String vaccinationAssociationId;

    private List<MedicalServiceSchemeEntity> modifiedMedicalServiceScheme;
    private List<MedicalServiceSchemeEntity> excludedMedicalServiceScheme;
    private List<MedicalTestSchemeEntity> modifiedMedicalTestScheme;
    private List<MedicalTestSchemeEntity> excludedMedicalTestScheme;
    private List<VaccinationSchemeEntity> modifiedVaccinationScheme;
    private List<VaccinationSchemeEntity> excludedVaccinationScheme;
    private List<MedicalServiceEntity> medicalServiceDetails;
    private List<MedicalTestEntity> medicalTestDetails;
    private List<VaccinationEntity> vaccinationDetails;
}
