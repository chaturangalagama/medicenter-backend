package com.ilt.cms.api.entity.coverage;

import com.ilt.cms.api.entity.billPayment.CopayAmount;
import com.ilt.cms.api.entity.common.Relationship;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class CoveragePlanEntity {
    public enum LimitResetType {
        CALENDAR, FROM_FIRST_USE, FROM_POLICY_HOLDER_CREATION_DATE
    }

    private String id;
    private String name;
    private CapLimiterEntity capPerVisit = new CapLimiterEntity();
    private CapLimiterEntity capPerDay = new CapLimiterEntity();
    private CapLimiterEntity capPerWeek = new CapLimiterEntity();
    private CapLimiterEntity capPerMonth = new CapLimiterEntity();
    private CapLimiterEntity capPerYear = new CapLimiterEntity();
    private CapLimiterEntity capPerLifeTime = new CapLimiterEntity();
    private CopayAmount copayment;
    private LimitResetType limitResetType;
    private String code;
    private String remarks;
    private String clinicRemarks;
    private String registrationRemarks;
    private String paymentRemarks;
    private List<String> excludedClinics;
    private boolean excludeAllByDefault;
    private List<MedicalServiceSchemeEntity> includedMedicalServiceSchemes;
    private List<MedicalServiceSchemeEntity> excludedMedicalServiceSchemes;
    private List<Relationship> allowedRelationship;
    private boolean filterDiagnosisCode;
    private int minimumNumberOfDiagnosisCodes;



    public CoveragePlanEntity() {
        includedMedicalServiceSchemes = new ArrayList<>();
        excludedMedicalServiceSchemes = new ArrayList<>();
        allowedRelationship = new ArrayList<>();
        excludedClinics = new ArrayList<>();
    }

    public CoveragePlanEntity(String id, String name) {
        this();
        this.id = id;
        this.name = name;
    }

    /**
     * Sets an empty list
     */
    public void cleanDefaultSchemes() {
        includedMedicalServiceSchemes = new ArrayList<MedicalServiceSchemeEntity>();
        excludedMedicalServiceSchemes = new ArrayList<MedicalServiceSchemeEntity>();
    }

    public void clearIncludedList() {
        includedMedicalServiceSchemes.clear();
    }

    public void clearExcludedList() {
        excludedMedicalServiceSchemes.clear();
    }

    public boolean addSchemesToIncluded(MedicalServiceSchemeEntity medicalServiceScheme) {
        return addScheme(medicalServiceScheme, includedMedicalServiceSchemes);
    }

    public boolean addSchemesToExcluded(MedicalServiceSchemeEntity medicalServiceScheme) {
        return addScheme(medicalServiceScheme, excludedMedicalServiceSchemes);
    }

    private boolean addScheme(MedicalServiceSchemeEntity medicalServiceScheme, List<MedicalServiceSchemeEntity> medicalServiceSchemes) {
        if (!medicalServiceSchemes.contains(medicalServiceScheme)) {
            return medicalServiceSchemes.add(medicalServiceScheme);
        } else {
            return false;
        }
    }

    public void removeSchemesFromIncluded(String schemeId) {
        includedMedicalServiceSchemes = includedMedicalServiceSchemes.stream()
                .filter(scheme -> !scheme.getMedicalServiceItemID().equals(schemeId))
                .collect(Collectors.toList());
    }

    public void removeSchemesFromExcluded(String schemeId) {
        includedMedicalServiceSchemes = includedMedicalServiceSchemes.stream()
                .filter(scheme -> !scheme.getMedicalServiceItemID().equals(schemeId))
                .collect(Collectors.toList());
    }

    /**
     * Do not update medical schemes
     *
     * @param coveragePlan
     */
    public void updatePlanDetails(CoveragePlanEntity coveragePlan) {
        name = coveragePlan.name;
        capPerVisit = coveragePlan.capPerVisit;
        capPerMonth = coveragePlan.capPerMonth;
        capPerYear = coveragePlan.capPerYear;
        capPerWeek = coveragePlan.capPerWeek;
        capPerLifeTime = coveragePlan.capPerLifeTime;
        capPerDay = coveragePlan.capPerDay;
        limitResetType = coveragePlan.limitResetType;
        copayment = coveragePlan.copayment;
        code = coveragePlan.code;
        remarks = coveragePlan.remarks;
        excludedClinics = new ArrayList<>(coveragePlan.excludedClinics);

        clinicRemarks = coveragePlan.clinicRemarks;
        registrationRemarks = coveragePlan.registrationRemarks;
        paymentRemarks = coveragePlan.paymentRemarks;

        includedMedicalServiceSchemes.clear();
        includedMedicalServiceSchemes.addAll(coveragePlan.includedMedicalServiceSchemes);

        excludedMedicalServiceSchemes.clear();
        excludedMedicalServiceSchemes.addAll(coveragePlan.excludedMedicalServiceSchemes);
        filterDiagnosisCode = coveragePlan.isFilterDiagnosisCode();
        minimumNumberOfDiagnosisCodes = coveragePlan.minimumNumberOfDiagnosisCodes;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoveragePlanEntity that = (CoveragePlanEntity) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
