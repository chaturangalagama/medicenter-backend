package com.ilt.cms.core.entity;

import com.ilt.cms.core.entity.common.CorporateAddress;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.lippo.commons.util.CommonUtils.isStringValid;

public class Clinic extends PersistedObject {

    private String name;
    private String groupName;
    private String emailAddress;
    private CorporateAddress address;
    private String faxNumber;
    private String contactNumber;
    private Status status;
    private String companyRegistrationNumber;
    private String gstRegistrationNumber;
    private String heCode;



    @Indexed(unique = true)
    private String clinicCode;
    private List<String> attendingDoctorId = new ArrayList<>();
    private List<String> clinicStaffUsernames = new ArrayList<>();
    private String domain;
    private String uen;

    public Clinic(String name, String groupName, String emailAddress, CorporateAddress address, String faxNumber, String contactNumber,
                  Status status, String clinicCode, List<String> attendingDoctorId, List<String> clinicStaffUsernames) {
        this.name = name;
        this.groupName = groupName;
        this.emailAddress = emailAddress;
        this.address = address;
        this.faxNumber = faxNumber;
        this.contactNumber = contactNumber;
        this.status = status;
        this.clinicCode = clinicCode;
        this.attendingDoctorId = attendingDoctorId;
        this.clinicStaffUsernames = clinicStaffUsernames;
    }

    public Clinic() {
    }

    public boolean areParametersValid() {
        return isStringValid(name, contactNumber, clinicCode, groupName) && address != null && address.areParametersValid();
    }

    public void copy(Clinic clinic) {
        name = clinic.name;
        groupName = clinic.groupName;
        address = clinic.address;
        contactNumber = clinic.contactNumber;
        clinicCode = clinic.clinicCode;
        attendingDoctorId = new ArrayList<>(clinic.attendingDoctorId);
        clinicStaffUsernames = new ArrayList<>(clinic.clinicStaffUsernames);
        faxNumber = clinic.faxNumber;
        emailAddress = clinic.emailAddress;
        status = clinic.status;
    }
    
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CorporateAddress getAddress() {
        return address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getClinicCode() {
        return clinicCode;
    }

    public void setClinicCode(String clinicCode) {
        this.clinicCode = clinicCode;
    }


    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public void setAttendingDoctorId(List<String> attendingDoctorId) {
        this.attendingDoctorId = attendingDoctorId;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public List<String> getAttendingDoctorId() {
        return attendingDoctorId;
    }

    public List<String> getClinicStaffUsernames() {
        return clinicStaffUsernames;
    }

    public void setClinicStaffUsernames(List<String> clinicStaffUsernames) {
        this.clinicStaffUsernames = clinicStaffUsernames;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setAddress(CorporateAddress address) {
        this.address = address;
    }

    public String getEmailAddress() {
        return emailAddress;
    }


    public Status getStatus() {
        return status;
    }

    public String getCompanyRegistrationNumber() {
        return companyRegistrationNumber;
    }

    public void setCompanyRegistrationNumber(String companyRegistrationNumber) {
        this.companyRegistrationNumber = companyRegistrationNumber;
    }

    public String getGstRegistrationNumber() {
        return gstRegistrationNumber;
    }

    public void setGstRegistrationNumber(String gstRegistrationNumber) {
        this.gstRegistrationNumber = gstRegistrationNumber;
    }

    public String getUen() {
        return uen;
    }

    public void setUen(String uen) {
        this.uen = uen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clinic clinic = (Clinic) o;
        return Objects.equals(name, clinic.name) &&
                Objects.equals(groupName, clinic.groupName) &&
                Objects.equals(clinicCode, clinic.clinicCode);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, groupName, clinicCode);
    }

    @Override
    public String toString() {
        return "Clinic{" +
                "name='" + name + '\'' +
                ", groupName='" + groupName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", address=" + address +
                ", faxNumber='" + faxNumber + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", status=" + status +
                ", companyRegistrationNumber='" + companyRegistrationNumber + '\'' +
                ", gstRegistrationNumber='" + gstRegistrationNumber + '\'' +
                ", clinicCode='" + clinicCode + '\'' +
                ", attendingDoctorId=" + attendingDoctorId +
                ", clinicStaffUsernames=" + clinicStaffUsernames +
                '}';
    }


    public String getHeCode() {
        return heCode;
    }

    public void setHeCode(String heCode) {
        this.heCode = heCode;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
