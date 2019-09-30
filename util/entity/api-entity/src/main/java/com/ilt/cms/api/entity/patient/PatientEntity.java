package com.ilt.cms.api.entity.patient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ilt.cms.api.entity.common.*;
import com.ilt.cms.api.entity.file.FileMetadataEntity;
import com.lippo.cms.util.CMSConstant;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PatientEntity {

    public enum Gender {
        MALE, FEMALE, OTHER
    }

    public enum MaritalStatus {
        SINGLE, MARRIED, DIVORCED, WIDOWED, SEPARATED, OTHER
    }
    private String id;
    private String title;
    private String preferredMethodOfCommunication;
    private boolean consentGiven;
    private String race;
    private String preferredLanguage;
    private String patientNumber;
    private PrimaryCareNetwork primaryCareNetwork;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
    private LocalDate registrationDate;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern=CMSConstant.JSON_DATE_FORMAT)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
    private LocalDate lastVisitedDate;
    private String name;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern=CMSConstant.JSON_DATE_FORMAT)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
    private LocalDate dob;
    private UserId userId;
    private Gender gender;
    private ContactNumber contactNumber;
    private ContactNumber secondaryNumber;
    private Status status;
    private Address address;
    private String emailAddress;
    private EmergencyContactNumber emergencyContactNumber;
    private PatientCompany company;
    private String nationality;
    private String companyId;
    private MaritalStatus maritalStatus;
    private String remarks;
    private List<PatientAllergy> allergies = new ArrayList<>();
    private List<PatientVaccination> patientVaccinations = new ArrayList<>();
    private List<OnGoingMedication> onGoingMedications = new ArrayList<>();

    private List<FileMetadataEntity> fileMetaData = new ArrayList<>();
}
