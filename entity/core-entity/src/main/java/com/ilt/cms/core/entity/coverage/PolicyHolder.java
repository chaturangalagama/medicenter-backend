package com.ilt.cms.core.entity.coverage;


import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.UserId;
import com.ilt.cms.core.entity.patient.Address;
import com.ilt.cms.core.entity.patient.ContactNumber;
import com.lippo.commons.util.CommonUtils;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;

import java.time.LocalDate;

public class PolicyHolder extends PersistedObject {

    private UserId identificationNumber;
    private String name;
    private String medicalCoverageId;
    private String planId;
    private String patientCoverageId;
    private String specialRemarks;
    private String costCenter;
    private Address address;
    private ContactNumber mobile;
    private ContactNumber office;
    private ContactNumber home;
    private RelationshipMapping relationship;
    private Status status = Status.ACTIVE;

    private LocalDate startDate;
    private LocalDate endDate;


    public boolean parametersValid() {
        return identificationNumber != null && identificationNumber.areParametersValid()
                && CommonUtils.isStringValid(name, patientCoverageId)
                && startDate != null;
    }

    public <T extends PolicyHolder> T copy(Class<T> type) {
        try {
            T holder = type.newInstance();
            holder.setIdentificationNumber(identificationNumber);
            holder.setName(name);
            holder.setMedicalCoverageId(medicalCoverageId);
            holder.setPlanId(planId);
            holder.setPatientCoverageId(patientCoverageId);
            holder.setSpecialRemarks(specialRemarks);
            holder.setStartDate(startDate);
            holder.setEndDate(endDate);
            holder.setAddress(address);
            holder.setMobile(mobile);
            holder.setOffice(office);
            holder.setHome(home);
            holder.setRelationship(relationship);
            holder.setStatus(status);
            holder.setCostCenter(costCenter);
            return holder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @CompoundIndexes({
            @CompoundIndex(name = "identification_number", def = "{'identificationNumber' : 1}"),
            @CompoundIndex(name = "index_medi_plan", def = "{'medicalCoverageId' : 1, 'planId' : 1}")
    })
    public static class PolicyHolderCorporate extends PolicyHolder {

    }

    @CompoundIndexes({
            @CompoundIndex(name = "index_unique", unique = true,
                    def = "{'identificationNumber' : 1, 'medicalCoverageId' : 1, 'planId' : 1}")
    })
    public static class PolicyHolderInsurance extends PolicyHolder {

    }

    @CompoundIndexes({
            @CompoundIndex(name = "index_unique", unique = true,
                    def = "{'identificationNumber' : 1, 'medicalCoverageId' : 1, 'planId' : 1}")
    })
    public static class PolicyHolderMediSave extends PolicyHolder {

    }

    @CompoundIndexes({
            @CompoundIndex(name = "index_unique", unique = true,
                    def = "{'identificationNumber' : 1, 'medicalCoverageId' : 1, 'planId' : 1}")

    })
    public static class PolicyHolderChas extends PolicyHolder {

    }

    public UserId getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(UserId identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMedicalCoverageId() {
        return medicalCoverageId;
    }

    public void setMedicalCoverageId(String medicalCoverageId) {
        this.medicalCoverageId = medicalCoverageId;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getPatientCoverageId() {
        return patientCoverageId;
    }

    public void setPatientCoverageId(String patientCoverageId) {
        this.patientCoverageId = patientCoverageId;
    }

    public String getSpecialRemarks() {
        return specialRemarks;
    }

    public void setSpecialRemarks(String specialRemarks) {
        this.specialRemarks = specialRemarks;
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

    public Address getAddress() {
        return address;
    }

    public ContactNumber getMobile() {
        return mobile;
    }

    public ContactNumber getOffice() {
        return office;
    }

    public ContactNumber getHome() {
        return home;
    }


    public void setAddress(Address address) {
        this.address = address;
    }

    public void setMobile(ContactNumber mobile) {
        this.mobile = mobile;
    }

    public void setOffice(ContactNumber office) {
        this.office = office;
    }

    public void setHome(ContactNumber home) {
        this.home = home;
    }

    public RelationshipMapping getRelationship() {
        return relationship;
    }

    public void setRelationship(RelationshipMapping relationship) {
        this.relationship = relationship;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    @Override
    public String toString() {
        return "PolicyHolder{" +
                "identificationNumber=" + identificationNumber +
                ", name='" + name + '\'' +
                ", medicalCoverageId='" + medicalCoverageId + '\'' +
                ", planId='" + planId + '\'' +
                ", patientCoverageId='" + patientCoverageId + '\'' +
                ", specialRemarks='" + specialRemarks + '\'' +
                ", costCenter='" + costCenter + '\'' +
                ", address=" + address +
                ", mobile=" + mobile +
                ", office=" + office +
                ", home=" + home +
                ", relationship=" + relationship +
                ", status=" + status +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
