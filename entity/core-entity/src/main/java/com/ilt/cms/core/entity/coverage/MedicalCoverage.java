package com.ilt.cms.core.entity.coverage;

import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.common.ContactPerson;
import com.ilt.cms.core.entity.common.CorporateAddress;
import com.lippo.commons.util.CommonUtils;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.lippo.commons.util.CommonUtils.isStringValid;

public class MedicalCoverage extends PersistedObject {

    public static final String ALL_ACCESS_LEVEL = "ALL_ACCESS";

    public enum CoverageType {
        CORPORATE, INSURANCE, MEDISAVE, CHAS
    }

    @Indexed(unique = true)
    private String name;
    private String code;
    private String accountManager;
    private CoverageType type;
    private LocalDate startDate;
    private LocalDate endDate;
    private int creditTerms;
    private String website;
    private List<String> costCenters = new ArrayList<>();
    private boolean payAtClinic;

    @Transient
    private int policyHolderCount;

    private boolean trackAttendance;
    private boolean usePatientAddressForBilling;
    private boolean medicineRefillAllowed;
    private boolean showDiscount;
    private boolean showMemberCard;

    private CorporateAddress address;

    private List<ContactPerson> contacts;

    private Status status;
    private List<CoveragePlan> coveragePlans;

    private Set<String> allowedAccessLevel;

    public MedicalCoverage() {
        coveragePlans = new ArrayList<>();
        allowedAccessLevel = new HashSet<>(Arrays.asList(ALL_ACCESS_LEVEL));
    }

    public void prepareFieldsForPersistence() {
        id = null;
        status = Status.ACTIVE;
        for (CoveragePlan coveragePlan : coveragePlans) {
            coveragePlan.setId(CommonUtils.idGenerator());
        }
    }

    public boolean areThePlansValid(List<String> clinics) {
        return isStringValid(accountManager, code, name)
                && coveragePlans.stream().allMatch(coveragePlan ->
                clinics.containsAll(coveragePlan.getExcludedClinics())
                        && isStringValid(coveragePlan.getCode(), coveragePlan.getName()));
    }

    public boolean userAllowedAccess(String accessRole) {
        return allowedAccessLevel.contains(ALL_ACCESS_LEVEL)
                || allowedAccessLevel.contains(accessRole);
    }

    /**
     * Checks if the plan is not already available and adds the new plan
     *
     * @param coveragePlan
     * @return
     */
    public boolean addNewPlan(CoveragePlan coveragePlan) {
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
    }


    /**
     * Does not update the CoverageType, Plans, and Code
     *
     * @param medicalCoverage
     */
    public void updateFields(MedicalCoverage medicalCoverage) {
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

    public static CoveragePlan findPlan(MedicalCoverage medicalCoverage, String planId) {
        if (medicalCoverage == null) {
            return null;
        }
        return medicalCoverage.getCoveragePlans()
                .stream().filter(coveragePlan -> coveragePlan.getId().equals(planId))
                .findFirst().orElse(null);
    }

    public CoveragePlan findPlan(String planId) {
        return getCoveragePlans()
                .stream().filter(coveragePlan -> coveragePlan.getId().equals(planId))
                .findFirst().orElse(null);
    }

    public boolean isContainPlan(String planId) {
        return getCoveragePlans().stream()
                .anyMatch(coveragePlan -> coveragePlan.getId().equals(planId));
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CoverageType getType() {
        return type;
    }

    public void setType(CoverageType type) {
        this.type = type;
    }

    public List<CoveragePlan> getCoveragePlans() {
        return coveragePlans;
    }

    public void setCoveragePlans(List<CoveragePlan> coveragePlans) {
        this.coveragePlans = coveragePlans;
    }

    public CorporateAddress getAddress() {
        return address;
    }

    public void setAddress(CorporateAddress address) {
        this.address = address;
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAccountManager() {
        return accountManager;
    }

    public void setAccountManager(String accountManager) {
        this.accountManager = accountManager;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getCreditTerms() {
        return creditTerms;
    }

    public void setCreditTerms(int creditTerms) {
        this.creditTerms = creditTerms;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public boolean isTrackAttendance() {
        return trackAttendance;
    }

    public void setTrackAttendance(boolean trackAttendance) {
        this.trackAttendance = trackAttendance;
    }

    public boolean isUsePatientAddressForBilling() {
        return usePatientAddressForBilling;
    }

    public void setUsePatientAddressForBilling(boolean usePatientAddressForBilling) {
        this.usePatientAddressForBilling = usePatientAddressForBilling;
    }

    public boolean isMedicineRefillAllowed() {
        return medicineRefillAllowed;
    }

    public void setMedicineRefillAllowed(boolean medicineRefillAllowed) {
        this.medicineRefillAllowed = medicineRefillAllowed;
    }

    public boolean isShowDiscount() {
        return showDiscount;
    }

    public void setShowDiscount(boolean showDiscount) {
        this.showDiscount = showDiscount;
    }

    public boolean isShowMemberCard() {
        return showMemberCard;
    }

    public void setShowMemberCard(boolean showMemberCard) {
        this.showMemberCard = showMemberCard;
    }

    public List<ContactPerson> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactPerson> contacts) {
        this.contacts = contacts;
    }


    public List<String> getCostCenters() {
        return costCenters;
    }

    public void setCostCenters(List<String> costCenters) {
        this.costCenters = costCenters;
    }

    public int getPolicyHolderCount() {
        return policyHolderCount;
    }

    public void setPolicyHolderCount(int policyHolderCount) {
        this.policyHolderCount = policyHolderCount;
    }


    public boolean isPayAtClinic() {
        return payAtClinic;
    }

    public void setPayAtClinic(boolean payAtClinic) {
        this.payAtClinic = payAtClinic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalCoverage that = (MedicalCoverage) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "MedicalCoverage{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", accountManager='" + accountManager + '\'' +
                ", type=" + type +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", creditTerms=" + creditTerms +
                ", website='" + website + '\'' +
                ", costCenters=" + costCenters +
                ", payAtClinic=" + payAtClinic +
                ", policyHolderCount=" + policyHolderCount +
                ", trackAttendance=" + trackAttendance +
                ", usePatientAddressForBilling=" + usePatientAddressForBilling +
                ", medicineRefillAllowed=" + medicineRefillAllowed +
                ", showDiscount=" + showDiscount +
                ", showMemberCard=" + showMemberCard +
                ", address=" + address +
                ", contacts=" + contacts +
                ", status=" + status +
                ", allowedAccessLevel=" + allowedAccessLevel +
                ", coveragePlans=" + coveragePlans +
                '}';
    }
}
