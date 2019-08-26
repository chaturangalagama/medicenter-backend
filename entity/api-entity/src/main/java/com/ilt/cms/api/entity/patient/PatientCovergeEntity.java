package com.ilt.cms.api.entity.patient;

import com.ilt.cms.api.entity.coverage.CapLimiterEntity;
import com.ilt.cms.api.entity.coverage.CoveragePlanEntity;
import com.ilt.cms.api.entity.coverage.MedicalCoverageEntity;
import com.ilt.cms.api.entity.coverage.MedicalServiceSchemeEntity;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PatientCovergeEntity {

    private String medicalCoverageId;
    private String medicalCoverageName;
    private String planName;
    private String planId;
    private MedicalCoverageEntity.CoverageType type;
    private CapLimiterEntity capPerVisit;
    private CapLimiterEntity capPerWeek;
    private CapLimiterEntity capPerMonth;
    private CapLimiterEntity capPerYear;
    private CapLimiterEntity capPerLifeTime;
    private String code;
    private String remarks;
    private List<String> excludedClinics;
    private List<MedicalServiceSchemeEntity> medicalServiceSchemes;

    public void populateFields(MedicalCoverageEntity coverage, CoveragePlanEntity coveragePlan) {
        medicalCoverageId = coverage.getId();
        medicalCoverageName = coverage.getName();
        planName = coveragePlan.getName();
        planId = coveragePlan.getId();
        type = coverage.getType();
        capPerVisit = coveragePlan.getCapPerVisit();
        capPerWeek = coveragePlan.getCapPerWeek();
        capPerMonth = coveragePlan.getCapPerMonth();
        capPerYear = coveragePlan.getCapPerYear();
        capPerLifeTime = coveragePlan.getCapPerLifeTime();
        code = coveragePlan.getCode();
        remarks = coveragePlan.getRemarks();
        excludedClinics = coveragePlan.getExcludedClinics();
        medicalServiceSchemes = coveragePlan.getIncludedMedicalServiceSchemes();
    }
}
