package com.ilt.cms.pm.integration.mapper.patient;

import com.ilt.cms.api.entity.file.FileMetadataEntity;
import com.ilt.cms.api.entity.patient.PatientEntity;
import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.common.Relationship;
import com.ilt.cms.core.entity.file.FileMetaData;
import com.ilt.cms.core.entity.patient.*;
import com.ilt.cms.core.entity.patient.Patient.Gender;
import com.ilt.cms.core.entity.patient.Patient.MaritalStatus;
import com.ilt.cms.pm.integration.mapper.Mapper;

import java.time.LocalDate;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PatientMapper extends Mapper {


    /**
     * Takes todays date as registration date
     * Does not copy over FileMetaDateEntity object
     *
     * @param patientEntity
     * @return
     */
    public static Patient mapToCore(PatientEntity patientEntity) {
        if(patientEntity == null){
            return null;
        }
        Patient patient = new Patient();
        patient.setId(patientEntity.getId());
        patient.setRegistrationDate(LocalDate.now());
        patient.setName(patientEntity.getName());
        patient.setDob(patientEntity.getDob());
        if(patientEntity.getUserId() != null) {
            patient.setUserId(mapToUserIdCore(patientEntity.getUserId()));
        }
        if(patientEntity.getGender() != null) {
            patient.setGender(Gender.valueOf(patientEntity.getGender().name()));
        }
        if(patientEntity.getContactNumber() != null) {
            patient.setContactNumber(new ContactNumber(patientEntity.getContactNumber().getNumber()));
        }
        patient.setEmailAddress(patientEntity.getEmailAddress());
        com.ilt.cms.api.entity.patient.EmergencyContactNumber emergencyContactNumber = patientEntity.getEmergencyContactNumber();
        if(emergencyContactNumber != null) {
            patient.setEmergencyContactNumber(new EmergencyContactNumber(emergencyContactNumber.getName(),
                    Relationship.valueOf(emergencyContactNumber.getRelationship().name()), emergencyContactNumber.getNumber()));
        }
        com.ilt.cms.api.entity.patient.PatientCompany company = patientEntity.getCompany();
        if(company != null) {
            patient.setCompany(new PatientCompany(company.getName(), company.getAddress(), company.getPostalCode(), company.getOccupation()));
        }
        patient.setNationality(patientEntity.getNationality());
        if(patientEntity.getMaritalStatus() != null) {
            patient.setMaritalStatus(MaritalStatus.valueOf(patientEntity.getMaritalStatus().name()));
        }
        patient.setRemarks(patientEntity.getRemarks());
        com.ilt.cms.api.entity.common.Address address = patientEntity.getAddress();
        if(address != null) {
            patient.setAddress(new Address(address.getAddress(), address.getCountry(), address.getPostalCode()));
        }
        patient.setLastVisitedDate(LocalDate.now());
        patient.setStatus(Status.ACTIVE);
        patient.setAllergies(patientEntity.getAllergies().stream()
                .map(mapAllergy())
                .collect(Collectors.toList()));
        patient.setTitle(patientEntity.getTitle());
        patient.setPreferredMethodOfCommunication(patientEntity.getPreferredMethodOfCommunication());
        patient.setConsentGiven(patientEntity.isConsentGiven());
        patient.setRace(patientEntity.getRace());
        patient.setPreferredLanguage(patientEntity.getPreferredLanguage());
        com.ilt.cms.api.entity.common.ContactNumber secondaryNumber = patientEntity.getSecondaryNumber();
        if(secondaryNumber != null) {
            patient.setSecondaryNumber(new ContactNumber(secondaryNumber.getNumber()));
        }
        if(patientEntity.getPatientVaccinations() != null){
            patient.setPatientVaccinations(patientEntity.getPatientVaccinations().stream().map(PatientMapper::mapToPatientVaccinationCore).collect(Collectors.toList()));
        }
        if(patientEntity.getFileMetaData() != null){
            patient.setFileMetaData(patientEntity.getFileMetaData().stream().map(PatientMapper::mapToFileMetaDataCore).collect(Collectors.toList()));
        }
        if(patientEntity.getPrimaryCareNetwork() != null){
            patient.setPrimaryCareNetwork(mapToPrimaryCareNetworkCore(patientEntity.getPrimaryCareNetwork()));
        }
        if(patientEntity.getOnGoingMedications() != null){
            patient.setOnGoingMedications(patientEntity.getOnGoingMedications().stream()
                    .map(onGoingMedicationEntity ->  mapToOnGoingMedicationCore(onGoingMedicationEntity))
                    .collect(Collectors.toList()));
        }
        return patient;
    }

    public static PatientEntity mapToEntity(Patient patient) {
        if(patient == null){
            return null;
        }
        PatientEntity patientEntity = new PatientEntity();
        patientEntity.setId(patient.getId());
        patientEntity.setRegistrationDate(patient.getRegistrationDate());
        patientEntity.setName(patient.getName());
        patientEntity.setDob(patient.getDob());
        if(patient.getUserId() != null) {
            patientEntity.setUserId(mapToUserIdEntity(patient.getUserId()));
        }
        if(patient.getGender() != null) {
            patientEntity.setGender(com.ilt.cms.api.entity.patient.PatientEntity.Gender.valueOf(patient.getGender().name()));
        }
        if(patient.getContactNumber() != null) {
            patientEntity.setContactNumber(new com.ilt.cms.api.entity.common.ContactNumber(patient.getContactNumber().getNumber()));
        }
        patientEntity.setEmailAddress(patient.getEmailAddress());
        EmergencyContactNumber emergencyContactNumber = patient.getEmergencyContactNumber();
        if(emergencyContactNumber != null) {
            patientEntity.setEmergencyContactNumber(new com.ilt.cms.api.entity.patient.EmergencyContactNumber(emergencyContactNumber.getName(),
                    com.ilt.cms.api.entity.common.Relationship.valueOf(String.valueOf(emergencyContactNumber.getRelationship())),
                            emergencyContactNumber.getNumber()));
        }

        PatientCompany company = patient.getCompany();
        if(company != null) {
            patientEntity.setCompany(new com.ilt.cms.api.entity.patient.PatientCompany(
                    company.getName(), company.getAddress(), company.getPostalCode(), company.getOccupation()));
        }
        patientEntity.setNationality(patient.getNationality());
        if(patient.getMaritalStatus() != null) {
            patientEntity.setMaritalStatus(com.ilt.cms.api.entity.patient.PatientEntity.MaritalStatus.valueOf(patient.getMaritalStatus().name()));
        }
        patientEntity.setRemarks(patient.getRemarks());
        Address address = patient.getAddress();
        if(address != null) {
            patientEntity.setAddress(new com.ilt.cms.api.entity.common.Address(address.getAddress(), address.getCountry(), address.getPostalCode()));
        }
        patientEntity.setLastVisitedDate(LocalDate.now());
        patientEntity.setStatus(com.ilt.cms.api.entity.common.Status.ACTIVE);
        patientEntity.setAllergies(patient.getAllergies().stream()
                .map(mapAllergyFromCore())
                .collect(Collectors.toList()));
        patientEntity.setTitle(patient.getTitle());
        patientEntity.setPreferredMethodOfCommunication(patient.getPreferredMethodOfCommunication());
        patientEntity.setConsentGiven(patient.isConsentGiven());
        patientEntity.setRace(patient.getRace());
        patientEntity.setPreferredLanguage(patient.getPreferredLanguage());
        ContactNumber secondaryNumber = patient.getSecondaryNumber();
        if(secondaryNumber != null) {
            patientEntity.setSecondaryNumber(new com.ilt.cms.api.entity.common.ContactNumber(secondaryNumber.getNumber()));
        }
        if(patient.getPatientVaccinations() != null) {
            patientEntity.setPatientVaccinations(patient.getPatientVaccinations().stream().map(PatientMapper::mapToPatientVaccinationEntity).collect(Collectors.toList()));
        }
        if(patient.getFileMetaData() != null) {
            patientEntity.setFileMetaData(patient.getFileMetaData().stream().map(PatientMapper::mapToFileMetaDataEntity).collect(Collectors.toList()));
        }
        if(patient.getPrimaryCareNetwork() != null){
            patientEntity.setPrimaryCareNetwork(mapToPrimaryCareNetworkEntity(patient.getPrimaryCareNetwork()));
        }
        if(patient.getOnGoingMedications() != null){
            patientEntity.setOnGoingMedications(patient.getOnGoingMedications().stream()
                    .map(onGoingMedication ->  mapToOnGoingMedicationEntity(onGoingMedication))
                    .collect(Collectors.toList()));
        }
        return patientEntity;
    }


    private static Function<com.ilt.cms.api.entity.patient.PatientAllergy, PatientAllergy> mapAllergy() {
        return patientAllergy ->
                new PatientAllergy(PatientAllergy.AllergyType.valueOf(patientAllergy.getAllergyType().name()),
                        patientAllergy.getName(), patientAllergy.getRemarks(), patientAllergy.getAddedDate());
    }

    private static Function<PatientAllergy, com.ilt.cms.api.entity.patient.PatientAllergy> mapAllergyFromCore() {
        return patientAllergy ->
                new com.ilt.cms.api.entity.patient.PatientAllergy(com.ilt.cms.api.entity.patient.PatientAllergy.AllergyType.valueOf(patientAllergy.getAllergyType().name()),
                        patientAllergy.getName(), patientAllergy.getRemarks(), patientAllergy.getAddedDate());
    }

    private static OnGoingMedication mapToOnGoingMedicationCore(com.ilt.cms.api.entity.patient.OnGoingMedication onGoingMedicationEntity){
        if(onGoingMedicationEntity == null){
            return null;
        }
        OnGoingMedication onGoingMedication = new OnGoingMedication();
        onGoingMedication.setItemCode(onGoingMedicationEntity.getItemCode());
        onGoingMedication.setMedicationName(onGoingMedicationEntity.getMedicationName());
        onGoingMedication.setStartDate(onGoingMedicationEntity.getStartDate());
        if(onGoingMedicationEntity.getType() != null) {
            onGoingMedication.setType(OnGoingMedication.OnGoingType.valueOf(onGoingMedicationEntity.getType().name()));
        }
        return onGoingMedication;
    }

    private static com.ilt.cms.api.entity.patient.OnGoingMedication mapToOnGoingMedicationEntity(OnGoingMedication onGoingMedication){
        if(onGoingMedication == null){
            return null;
        }
        com.ilt.cms.api.entity.patient.OnGoingMedication onGoingMedicationEntity = new com.ilt.cms.api.entity.patient.OnGoingMedication();
        onGoingMedicationEntity.setItemCode(onGoingMedication.getItemCode());
        onGoingMedicationEntity.setMedicationName(onGoingMedication.getMedicationName());
        onGoingMedicationEntity.setStartDate(onGoingMedication.getStartDate());
        if(onGoingMedication.getType() != null) {
            onGoingMedicationEntity.setType(com.ilt.cms.api.entity.patient.OnGoingMedication.OnGoingType.valueOf(onGoingMedication.getType().name()));
        }
        return onGoingMedicationEntity;
    }

    public static PatientVaccination mapToPatientVaccinationCore(com.ilt.cms.api.entity.patient.PatientVaccination patientVaccinationEntity){
        if(patientVaccinationEntity == null){
            return null;
        }
        PatientVaccination patientVaccination = new PatientVaccination();
        patientVaccination.setId(patientVaccinationEntity.getId());
        patientVaccination.setDoctorId(patientVaccinationEntity.getDoctorId());
        patientVaccination.setVaccineId(patientVaccinationEntity.getVaccineId());
        patientVaccination.setGivenDate(patientVaccinationEntity.getGivenDate());
        patientVaccination.setPlaceGiven(patientVaccinationEntity.getPlaceGiven());
        patientVaccination.setVaccinationSchedules(
                patientVaccinationEntity.getVaccinationSchedules().stream().map(PatientMapper.mapToVaccinationScheduleCore()).collect(Collectors.toList())
        );
        return patientVaccination;
    }

    public static com.ilt.cms.api.entity.patient.PatientVaccination mapToPatientVaccinationEntity(PatientVaccination patientVaccination){
        if(patientVaccination == null){
            return null;
        }
        com.ilt.cms.api.entity.patient.PatientVaccination patientVaccinationEntity = new com.ilt.cms.api.entity.patient.PatientVaccination();
        patientVaccinationEntity.setId(patientVaccination.getId());
        patientVaccinationEntity.setDoctorId(patientVaccination.getDoctorId());
        patientVaccinationEntity.setVaccineId(patientVaccination.getVaccineId());
        patientVaccinationEntity.setGivenDate(patientVaccination.getGivenDate());
        patientVaccinationEntity.setPlaceGiven(patientVaccination.getPlaceGiven());
        patientVaccinationEntity.setVaccinationSchedules(
                patientVaccination.getVaccinationSchedules().stream().map(PatientMapper.mapToVaccinationScheduleEntity()).collect(Collectors.toList())
        );
        return patientVaccinationEntity;
    }

    public static FileMetaData mapToFileMetaDataCore(FileMetadataEntity fileMetadataEntity){
        if(fileMetadataEntity == null){
            return null;
        }
        FileMetaData fileMetaData = new FileMetaData(fileMetadataEntity.decodedFileIdValue(), fileMetadataEntity.getName()
                , fileMetadataEntity.getFileName(), fileMetadataEntity.getUploader(), fileMetadataEntity.getClinicId(),
                fileMetadataEntity.getType(), fileMetadataEntity.getSize(), fileMetadataEntity.getDescription());
        return fileMetaData;

    }

    public static FileMetadataEntity mapToFileMetaDataEntity(FileMetaData fileMetadata){
        if(fileMetadata == null){
            return null;
        }
        FileMetadataEntity fileMetadataEntity = new FileMetadataEntity(fileMetadata.getFileId(), fileMetadata.getName()
                , fileMetadata.getFileName(), fileMetadata.getUploader(), fileMetadata.getClinicId(),
                fileMetadata.getType(), fileMetadata.getSize(), fileMetadata.getDescription());
        return fileMetadataEntity;

    }

    private static Function<com.ilt.cms.api.entity.patient.VaccinationSchedule, VaccinationSchedule> mapToVaccinationScheduleCore(){
        return vaccinationScheduleEntity ->
                new VaccinationSchedule(vaccinationScheduleEntity.getVaccineId(), vaccinationScheduleEntity.getScheduledDate());
    }

    private static Function<VaccinationSchedule, com.ilt.cms.api.entity.patient.VaccinationSchedule> mapToVaccinationScheduleEntity(){
        return vaccinationSchedule ->
                new com.ilt.cms.api.entity.patient.VaccinationSchedule(vaccinationSchedule.getVaccineId(), vaccinationSchedule.getScheduledDate());
    }

    private static PrimaryCareNetwork mapToPrimaryCareNetworkCore(com.ilt.cms.api.entity.patient.PrimaryCareNetwork primaryCareNetworkEntity){
        return new PrimaryCareNetwork(primaryCareNetworkEntity.isOptIn(), primaryCareNetworkEntity.isOptOut(), primaryCareNetworkEntity.getOptInDate());
    }

    private static com.ilt.cms.api.entity.patient.PrimaryCareNetwork mapToPrimaryCareNetworkEntity(PrimaryCareNetwork primaryCareNetwork){
        return new com.ilt.cms.api.entity.patient.PrimaryCareNetwork(primaryCareNetwork.isOptIn(),
                primaryCareNetwork.isOptOut(), primaryCareNetwork.getOptInDate());
    }
}
