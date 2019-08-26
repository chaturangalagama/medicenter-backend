package com.ilt.cms.core.entity.patient;

import com.ilt.cms.core.entity.coverage.CapLimiter;
import com.ilt.cms.core.entity.coverage.CoveragePlan;
import com.ilt.cms.core.entity.coverage.MedicalCoverage;
import com.ilt.cms.core.entity.coverage.MedicalServiceScheme;

import java.util.List;

public class PatientCoverage {

    private String medicalCoverageId;
    private String medicalCoverageName;
    private String planName;
    private String planId;
    private MedicalCoverage.CoverageType type;
    private CapLimiter capPerVisit;
    private CapLimiter capPerWeek;
    private CapLimiter capPerMonth;
    private CapLimiter capPerYear;
    private CapLimiter capPerLifeTime;
    private String code;
    private String remarks;
    private List<String> excludedClinics;

    public void populateFields(MedicalCoverage coverage, CoveragePlan coveragePlan) {
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
    }

    public String getMedicalCoverageId() {
        return medicalCoverageId;
    }

    public void setMedicalCoverageId(String medicalCoverageId) {
        this.medicalCoverageId = medicalCoverageId;
    }

    public String getMedicalCoverageName() {
        return medicalCoverageName;
    }

    public void setMedicalCoverageName(String medicalCoverageName) {
        this.medicalCoverageName = medicalCoverageName;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public MedicalCoverage.CoverageType getType() {
        return type;
    }

    public void setType(MedicalCoverage.CoverageType type) {
        this.type = type;
    }


    public CapLimiter getCapPerVisit() {
        return capPerVisit;
    }

    public void setCapPerVisit(CapLimiter capPerVisit) {
        this.capPerVisit = capPerVisit;
    }

    public CapLimiter getCapPerWeek() {
        return capPerWeek;
    }

    public void setCapPerWeek(CapLimiter capPerWeek) {
        this.capPerWeek = capPerWeek;
    }

    public CapLimiter getCapPerMonth() {
        return capPerMonth;
    }

    public void setCapPerMonth(CapLimiter capPerMonth) {
        this.capPerMonth = capPerMonth;
    }

    public CapLimiter getCapPerYear() {
        return capPerYear;
    }

    public void setCapPerYear(CapLimiter capPerYear) {
        this.capPerYear = capPerYear;
    }

    public CapLimiter getCapPerLifeTime() {
        return capPerLifeTime;
    }

    public void setCapPerLifeTime(CapLimiter capPerLifeTime) {
        this.capPerLifeTime = capPerLifeTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<String> getExcludedClinics() {
        return excludedClinics;
    }

    public void setExcludedClinics(List<String> excludedClinics) {
        this.excludedClinics = excludedClinics;
    }

}
