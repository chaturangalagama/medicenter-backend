package com.ilt.cms.core.entity.patient;


import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.UserId;
import com.ilt.cms.core.entity.file.FileMetaData;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.lippo.commons.util.CommonUtils.isStringValid;

public class Patient extends PersistedObject {

    public enum Gender {
        MALE, FEMALE, OTHER
    }

    public enum MaritalStatus {
        SINGLE, MARRIED, DIVORCED, WIDOWED, SEPARATED, OTHER
    }

    private String title;
    private String preferredMethodOfCommunication;
    private boolean consentGiven;
    private String race;
    private String preferredLanguage;
    private String patientNumber;
    private PrimaryCareNetwork primaryCareNetwork;

    private LocalDate registrationDate;
    private LocalDate lastVisitedDate;
    @TextIndexed
    private String name;
    private LocalDate dob;
    @Indexed(unique = true)
    @TextIndexed
    private UserId userId;
    private Gender gender;
    @TextIndexed
    private ContactNumber contactNumber;
    private ContactNumber secondaryNumber;
    private Status status;
    private Address address;
    private String emailAddress;
    private EmergencyContactNumber emergencyContactNumber;
    private PatientCompany company;
    private String nationality;
    @Indexed
    private String companyId;
    private MaritalStatus maritalStatus;
    private String remarks;
    private List<PatientAllergy> allergies = new ArrayList<>();
    private List<PatientVaccination> patientVaccinations = new ArrayList<>();
    private List<OnGoingMedication> onGoingMedications = new ArrayList<>();
    //not stored part of patient collection
    private List<FileMetaData> fileMetaData = new ArrayList<>();
    private List<PatientPayable> patientPayables;

    public String formulateAddress() {
        return address.getAddress() + "\n" + address.getPostalCode() + "\n" + address.getCountry();
    }

    public boolean removePatientVaccination(String patientVaccinationId) {
        return patientVaccinations.removeIf(vaccination -> vaccination.getId().equals(patientVaccinationId));
    }

    public void resetNewPatientRegistrationDetails() {
        super.id = null;
        patientVaccinations.clear();
        status = Status.ACTIVE;
    }

    public boolean addPatientVaccination(PatientVaccination vaccination) {
        return patientVaccinations.add(vaccination);
    }

    public boolean parameterValuesValid() {
        boolean stringValid = isStringValid(name);
        boolean parametersValid = address.areParametersValid();
        boolean parametersValid2 = contactNumber.areParametersValid();
        boolean parameterValidUserId = userId.areParametersValid();
        boolean parameterValidOnGoingMedication = onGoingMedications.stream().allMatch(onGoingMedication -> onGoingMedication.parametersValid());
        boolean pcn = true;
        if(primaryCareNetwork != null){
            pcn = primaryCareNetwork.areParametersValid();
        }
        return stringValid
                && dob != null
                && parametersValid
                && parametersValid2
                && parameterValidOnGoingMedication
                && maritalStatus != null
                && parameterValidUserId
                && gender != null
                && pcn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPreferredMethodOfCommunication() {
        return preferredMethodOfCommunication;
    }

    public void setPreferredMethodOfCommunication(String preferredMethodOfCommunication) {
        this.preferredMethodOfCommunication = preferredMethodOfCommunication;
    }

    public boolean isConsentGiven() {
        return consentGiven;
    }

    public void setConsentGiven(boolean consentGiven) {
        this.consentGiven = consentGiven;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getPatientNumber() {
        return patientNumber;
    }

    public void setPatientNumber(String patientNumber) {
        this.patientNumber = patientNumber;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDate getLastVisitedDate() {
        return lastVisitedDate;
    }

    public void setLastVisitedDate(LocalDate lastVisitedDate) {
        this.lastVisitedDate = lastVisitedDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public UserId getUserId() {
        if (userId == null) userId = new UserId();
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public ContactNumber getContactNumber() {
        if (contactNumber == null) contactNumber = new ContactNumber();
        return contactNumber;
    }

    public void setContactNumber(ContactNumber contactNumber) {
        this.contactNumber = contactNumber;
    }

    public ContactNumber getSecondaryNumber() {
        return secondaryNumber;
    }

    public void setSecondaryNumber(ContactNumber secondaryNumber) {
        this.secondaryNumber = secondaryNumber;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Address getAddress() {
        if (address == null) address = new Address();
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public EmergencyContactNumber getEmergencyContactNumber() {
        return emergencyContactNumber;
    }

    public void setEmergencyContactNumber(EmergencyContactNumber emergencyContactNumber) {
        this.emergencyContactNumber = emergencyContactNumber;
    }

    public PatientCompany getCompany() {
        return company;
    }

    public void setCompany(PatientCompany company) {
        this.company = company;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<PatientAllergy> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<PatientAllergy> allergies) {
        this.allergies = allergies;
    }

    public List<PatientVaccination> getPatientVaccinations() {
        return patientVaccinations;
    }

    public void setPatientVaccinations(List<PatientVaccination> patientVaccinations) {
        this.patientVaccinations = patientVaccinations;
    }

    public List<OnGoingMedication> getOnGoingMedications() {
        return onGoingMedications;
    }

    public void setOnGoingMedications(List<OnGoingMedication> onGoingMedications) {
        this.onGoingMedications = onGoingMedications;
    }

    public PrimaryCareNetwork getPrimaryCareNetwork() {
        return primaryCareNetwork;
    }

    public void setPrimaryCareNetwork(PrimaryCareNetwork primaryCareNetwork) {
        this.primaryCareNetwork = primaryCareNetwork;
    }

    public List<FileMetaData> getFileMetaData() {
        return fileMetaData;
    }

    public void setFileMetaData(List<FileMetaData> fileMetaData) {
        this.fileMetaData = fileMetaData;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "title='" + title + '\'' +
                ", preferredMethodOfCommunication='" + preferredMethodOfCommunication + '\'' +
                ", consentGiven=" + consentGiven +
                ", race='" + race + '\'' +
                ", preferredLanguage='" + preferredLanguage + '\'' +
                ", patientNumber='" + patientNumber + '\'' +
                ", registrationDate=" + registrationDate +
                ", lastVisitedDate=" + lastVisitedDate +
                ", name='" + name + '\'' +
                ", dob=" + dob +
                ", userId=" + userId +
                ", gender=" + gender +
                ", contactNumber=" + contactNumber +
                ", secondaryNumber=" + secondaryNumber +
                ", status=" + status +
                ", address=" + address +
                ", emailAddress='" + emailAddress + '\'' +
                ", emergencyContactNumber=" + emergencyContactNumber +
                ", company=" + company +
                ", nationality='" + nationality + '\'' +
                ", companyId='" + companyId + '\'' +
                ", maritalStatus=" + maritalStatus +
                ", remarks='" + remarks + '\'' +
                ", allergies=" + allergies +
                ", onGoingMedications=" + onGoingMedications +
                ", patientVaccinations=" + patientVaccinations +
                ", fileMetaData=" + fileMetaData +
                ", primaryCareNetwork=" + primaryCareNetwork +
                '}';
    }

    public void addPatientPayable(PatientPayable patientPayable) {
        if (patientPayables == null) {
            patientPayables = new ArrayList<>();
        }
        patientPayables.add(patientPayable);
    }

    public List<PatientPayable> getPatientPayables() {
        return patientPayables;
    }

    public void setPatientPayables(List<PatientPayable> patientPayables) {
        this.patientPayables = patientPayables;
    }

}
