package com.ilt.cms.api.entity.coverage;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ilt.cms.api.entity.common.ContactPerson;
import com.ilt.cms.api.entity.common.CorporateAddress;
import com.ilt.cms.api.entity.common.Status;
import com.lippo.cms.util.CMSConstant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@ToString
public class MedicalCoverageEntity {
    public enum CoverageType {
        CORPORATE, INSURANCE, MEDISAVE, CHAS
    }
    private String id;
    private String name;
    private String code;
    private String accountManager;
    private CoverageType type;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
    private LocalDate startDate;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern=CMSConstant.JSON_DATE_FORMAT)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
    private LocalDate endDate;
    private int creditTerms;
    private String website;
    private List<String> costCenters = new ArrayList<>();
    private boolean payAtClinic;

    private int policyHolderCount;

    private boolean trackAttendance;
    private boolean usePatientAddressForBilling;
    private boolean medicineRefillAllowed;
    private boolean showDiscount;
    private boolean showMemberCard;

    private CorporateAddress address;

    private List<ContactPerson> contacts;

    private Status status;
    private List<CoveragePlanEntity> coveragePlans;
    private Set<String> allowedAccessLevel;

    public MedicalCoverageEntity() {
        coveragePlans = new ArrayList<>();
        allowedAccessLevel = new HashSet<>();
    }

    /*public void prepareFieldsForPersistence() {
        id = null;
        status = Status.ACTIVE;
        for (CoveragePlan coveragePlan : coveragePlans) {
            coveragePlan.setId(CommonUtils.idGenerator());
            coveragePlan.cleanDefaultSchemes();
        }
    }

    public boolean areThePlansValid(List<String> clinics) {
        return isStringValid(accountManager, code, name)
                && coveragePlans.stream().allMatch(coveragePlan ->
                clinics.containsAll(coveragePlan.getExcludedClinics())
                        && isStringValid(coveragePlan.getCode(), coveragePlan.getName()));
    }*/

    /**
     * Checks if the plan is not already available and adds the new plan
     *
     * @param coveragePlan
     * @return
     */
    /*public boolean addNewPlan(CoveragePlanEntity coveragePlan) {
        if (Collections.frequency(coveragePlans, coveragePlan) == 0) {
            coveragePlan.setId(CommonUtils.idGenerator());
            return coveragePlans.add(coveragePlan);
        } else {
            return false;
        }
    }

    public void removePlan(String planId) {
        coveragePlans = coveragePlans.stream()
                .filter(coveragePlan -> !coveragePlan.getId().equals(planId))
                .collect(Collectors.toList());
    }*/


    /**
     * Does not update the CoverageType, Plans, and Code
     *
     * @param medicalCoverage
     */
    public void updateFields(MedicalCoverageEntity medicalCoverage) {
        setName(medicalCoverage.name);
        setAddress(medicalCoverage.address);
        setStatus(medicalCoverage.status);
        setAccountManager(medicalCoverage.accountManager);
        setStartDate(medicalCoverage.startDate);
        setEndDate(medicalCoverage.endDate);
        setCreditTerms(medicalCoverage.creditTerms);
        setWebsite(medicalCoverage.website);
        setTrackAttendance(medicalCoverage.trackAttendance);
        setUsePatientAddressForBilling(medicalCoverage.usePatientAddressForBilling);
        setMedicineRefillAllowed(medicalCoverage.medicineRefillAllowed);
        setShowDiscount(medicalCoverage.showDiscount);
        setShowMemberCard(medicalCoverage.showMemberCard);
        if (contacts != null) {
            contacts.clear();
            contacts.addAll(medicalCoverage.contacts);
        } else {
            setContacts(new ArrayList<>(medicalCoverage.contacts));
        }
        if (costCenters != null) {
            costCenters.clear();
            costCenters.addAll(medicalCoverage.costCenters);
        } else {
            setCostCenters(new ArrayList<>(medicalCoverage.costCenters));
        }
    }

    public static CoveragePlanEntity findPlan(MedicalCoverageEntity medicalCoverage, String planId) {
        return medicalCoverage.getCoveragePlans()
                .stream().filter(coveragePlan -> coveragePlan.getId().equals(planId))
                .findFirst().orElse(null);
    }

    public CoveragePlanEntity findPlan(String planId) {
        return getCoveragePlans()
                .stream().filter(coveragePlan -> coveragePlan.getId().equals(planId))
                .findFirst().orElse(null);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalCoverageEntity that = (MedicalCoverageEntity) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }

}
