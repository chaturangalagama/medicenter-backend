package com.ilt.cms.core.entity.coverage;

import com.ilt.cms.core.entity.CopayAmount;
import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.common.Relationship;
import com.lippo.commons.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CoveragePlan {

    public enum LimitResetType {
        CALENDAR, FROM_FIRST_USE, FROM_POLICY_HOLDER_CREATION_DATE
    }

    private String id;
    private String name;
    private CapLimiter capPerVisit = new CapLimiter();
    private CapLimiter capPerDay = new CapLimiter();
    private CapLimiter capPerWeek = new CapLimiter();
    private CapLimiter capPerMonth = new CapLimiter();
    private CapLimiter capPerYear = new CapLimiter();
    private CapLimiter capPerLifeTime = new CapLimiter();
    private CopayAmount copayment;
    private LimitResetType limitResetType;
    private String code;
    private String remarks;
    private String clinicRemarks;
    private String registrationRemarks;
    private String paymentRemarks;
    private List<String> excludedClinics;
    private boolean excludeAllByDefault;
    private List<Relationship> allowedRelationship;
    private boolean filterDiagnosisCode;
    private int minimumNumberOfDiagnosisCodes;
    private Status status;

    public boolean arePlansValid() {
        return CommonUtils.isStringValid(name, code);
    }


    public CoveragePlan() {

        allowedRelationship = new ArrayList<>();
        excludedClinics = new ArrayList<>();
    }

    public CoveragePlan(String id, String name) {
        this();
        this.id = id;
        this.name = name;
    }

    private boolean addScheme(MedicalServiceScheme medicalServiceScheme, List<MedicalServiceScheme> medicalServiceSchemes) {
        if (!medicalServiceSchemes.contains(medicalServiceScheme)) {
            return medicalServiceSchemes.add(medicalServiceScheme);
        } else {
            return false;
        }
    }

    /**
     * Do not update medical schemes
     *
     * @param coveragePlan
     */
    public void updatePlanDetails(CoveragePlan coveragePlan) {
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

        filterDiagnosisCode = coveragePlan.isFilterDiagnosisCode();
        minimumNumberOfDiagnosisCodes = coveragePlan.minimumNumberOfDiagnosisCodes;
    }


    public CoveragePlan(String id) {
        this(id, null);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoveragePlan that = (CoveragePlan) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public CopayAmount getCopayment() {
        return copayment;
    }

    public void setCopayment(CopayAmount copayment) {
        this.copayment = copayment;
    }

    public LimitResetType getLimitResetType() {
        return limitResetType;
    }

    public void setLimitResetType(LimitResetType limitResetType) {
        this.limitResetType = limitResetType;
    }

    public List<Relationship> getAllowedRelationship() {
        return allowedRelationship;
    }

    public void setAllowedRelationship(List<Relationship> allowedRelationship) {
        this.allowedRelationship = allowedRelationship;
    }

    public CapLimiter getCapPerVisit() {
        return capPerVisit;
    }

    public void setCapPerVisit(CapLimiter capPerVisit) {
        this.capPerVisit = capPerVisit;
    }

    public CapLimiter getCapPerMonth() {
        return capPerMonth;
    }

    public void setCapPerMonth(CapLimiter capPerMonth) {
        this.capPerMonth = capPerMonth;
    }

    public CapLimiter getCapPerWeek() {
        return capPerWeek;
    }

    public void setCapPerWeek(CapLimiter capPerWeek) {
        this.capPerWeek = capPerWeek;
    }

    public CapLimiter getCapPerLifeTime() {
        return capPerLifeTime;
    }

    public void setCapPerLifeTime(CapLimiter capPerLifeTime) {
        this.capPerLifeTime = capPerLifeTime;
    }

    public CapLimiter getCapPerYear() {
        return capPerYear;
    }

    public void setCapPerYear(CapLimiter capPerYear) {
        this.capPerYear = capPerYear;
    }

    public CapLimiter getCapPerDay() {
        return capPerDay;
    }

    public boolean isExcludeAllByDefault() {
        return excludeAllByDefault;
    }

    public void setExcludeAllByDefault(boolean excludeAllByDefault) {
        this.excludeAllByDefault = excludeAllByDefault;
    }

    public void setCapPerDay(CapLimiter capPerDay) {
        this.capPerDay = capPerDay;
    }

    public String getClinicRemarks() {
        return clinicRemarks;
    }

    public void setClinicRemarks(String clinicRemarks) {
        this.clinicRemarks = clinicRemarks;
    }

    public String getRegistrationRemarks() {
        return registrationRemarks;
    }

    public void setRegistrationRemarks(String registrationRemarks) {
        this.registrationRemarks = registrationRemarks;
    }

    public String getPaymentRemarks() {
        return paymentRemarks;
    }

    public void setPaymentRemarks(String paymentRemarks) {
        this.paymentRemarks = paymentRemarks;
    }

    public boolean isFilterDiagnosisCode() {
        return filterDiagnosisCode;
    }

    public void setFilterDiagnosisCode(boolean filterDiagnosisCode) {
        this.filterDiagnosisCode = filterDiagnosisCode;
    }

    public int getMinimumNumberOfDiagnosisCodes() {
        return minimumNumberOfDiagnosisCodes;
    }

    public void setMinimumNumberOfDiagnosisCodes(int minimumNumberOfDiagnosisCodes) {
        this.minimumNumberOfDiagnosisCodes = minimumNumberOfDiagnosisCodes;
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CoveragePlan{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", capPerVisit=" + capPerVisit +
                ", capPerDay=" + capPerDay +
                ", capPerWeek=" + capPerWeek +
                ", capPerMonth=" + capPerMonth +
                ", capPerYear=" + capPerYear +
                ", capPerLifeTime=" + capPerLifeTime +
                ", copayment=" + copayment +
                ", limitResetType=" + limitResetType +
                ", code='" + code + '\'' +
                ", remarks='" + remarks + '\'' +
                ", clinicRemarks='" + clinicRemarks + '\'' +
                ", registrationRemarks='" + registrationRemarks + '\'' +
                ", paymentRemarks='" + paymentRemarks + '\'' +
                ", excludedClinics=" + excludedClinics +
                ", excludeAllByDefault=" + excludeAllByDefault +
                ", allowedRelationship=" + allowedRelationship +
                ", filterDiagnosisCode=" + filterDiagnosisCode +
                ", minimumNumberOfDiagnosisCodes=" + minimumNumberOfDiagnosisCodes +
                ", status=" + status +
                '}';
    }

}
