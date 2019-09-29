package com.ilt.cms.pm.integration.mapper.patient;

import com.ilt.cms.api.entity.patient.MedicalAlertEntity;
import com.ilt.cms.core.entity.patient.MedicalAlert;

import java.util.List;
import java.util.stream.Collectors;

public class MedicalAlertMapper {
    public static MedicalAlertEntity mapToEntity(MedicalAlert medicalAlert) {
        if(medicalAlert == null){
            return null;
        }
        MedicalAlertEntity medicalAlertEntity = new MedicalAlertEntity();
        medicalAlertEntity.setId(medicalAlert.getId());
        medicalAlertEntity.setPatientId(medicalAlert.getPatientId());
        List<MedicalAlert.MedicalAlertDetails> medicalAlertDetailsList = medicalAlert.getDetails();

        List<MedicalAlertEntity.MedicalAlertDetails> medicalAlertEntityDetailsList =
        medicalAlertDetailsList.stream().map(MedicalAlertMapper::mapToEntity).collect(Collectors.toList());
        medicalAlertEntity.setDetails(medicalAlertEntityDetailsList);

        return medicalAlertEntity;
    }

    public static MedicalAlert mapToCore(MedicalAlertEntity medicalAlertEntity) {
        if(medicalAlertEntity == null){
            return null;
        }
        MedicalAlert medicalAlert = new MedicalAlert();
        medicalAlert.setId(medicalAlertEntity.getId());
        medicalAlert.setPatientId(medicalAlert.getPatientId());
        List<MedicalAlertEntity.MedicalAlertDetails> medicalAlertEntityDetailsList = medicalAlertEntity.getDetails();

        List<MedicalAlert.MedicalAlertDetails> medicalAlertDetailsList =
        medicalAlertEntityDetailsList.stream().map(MedicalAlertMapper::mapToCore).collect(Collectors.toList());
        medicalAlert.setDetails(medicalAlertDetailsList);

        return medicalAlert;
    }

    public static MedicalAlertEntity.MedicalAlertDetails mapToEntity(MedicalAlert.MedicalAlertDetails medicalAlertDetails) {
        if(medicalAlertDetails == null){
            return null;
        }
        MedicalAlertEntity.MedicalAlertDetails medicalAlertEntity = new MedicalAlertEntity.MedicalAlertDetails();
        medicalAlertEntity.setAlertId(medicalAlertDetails.getAlertId());
        if(medicalAlertDetails != null) {
            medicalAlertEntity.setAlertType(medicalAlertDetails.getAlertType().name());
        }
        medicalAlertEntity.setName(medicalAlertDetails.getName());
        medicalAlertEntity.setRemark(medicalAlertDetails.getRemark());
        if(medicalAlertDetails.getPriority() != null) {
            medicalAlertEntity.setPriority(MedicalAlertEntity.MedicalAlertDetails.Priority.valueOf(medicalAlertDetails.getPriority().name()));
        }
        medicalAlertEntity.setAddedDate(medicalAlertDetails.getAddedDate());
        medicalAlertEntity.setExpiryDate(medicalAlertDetails.getExpiryDate());
        return medicalAlertEntity;
    }

    public static MedicalAlert.MedicalAlertDetails mapToCore(MedicalAlertEntity.MedicalAlertDetails medicalAlertEntitiyDetails) {
        if(medicalAlertEntitiyDetails == null){
            return null;
        }
        MedicalAlert.MedicalAlertDetails medicalAlertDetails = new MedicalAlert.MedicalAlertDetails();
        medicalAlertDetails.setAlertId(medicalAlertEntitiyDetails.getAlertId());

        medicalAlertDetails.setAlertType(MedicalAlert.AlertType.valueOf(medicalAlertEntitiyDetails.getAlertType()));
        medicalAlertDetails.setName(medicalAlertEntitiyDetails.getName());
        medicalAlertDetails.setRemark(medicalAlertEntitiyDetails.getRemark());
        if(medicalAlertDetails.getPriority() != null) {
            medicalAlertDetails.setPriority(MedicalAlert.MedicalAlertDetails.Priority.valueOf(medicalAlertEntitiyDetails.getPriority().name()));
        }
        medicalAlertDetails.setAddedDate(medicalAlertEntitiyDetails.getAddedDate());
        medicalAlertDetails.setExpiryDate(medicalAlertEntitiyDetails.getExpiryDate());

        return medicalAlertDetails;
    }
}
