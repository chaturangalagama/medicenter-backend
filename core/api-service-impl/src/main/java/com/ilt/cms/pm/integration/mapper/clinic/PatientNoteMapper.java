package com.ilt.cms.pm.integration.mapper.clinic;

import com.ilt.cms.api.entity.patient.PatientNoteEntity;
import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.patient.PatientNote;

import java.util.stream.Collectors;

public class PatientNoteMapper {

    public static PatientNote mapToCore(PatientNoteEntity patientNoteEntity){
        if(patientNoteEntity == null){
            return null;
        }
        PatientNote patientNote = new PatientNote(patientNoteEntity.getPatientId());
        patientNote.setId(patientNoteEntity.getId());
        patientNote.setNoteDetails(patientNoteEntity.getNoteDetails().stream().map(PatientNoteMapper::mapToPatientNoteDetailsCore).collect(Collectors.toList()));
        return patientNote;

    }

    public static PatientNoteEntity mapToEntity(PatientNote patientNote)
    {
        if(patientNote == null){
            return null;
        }
        PatientNoteEntity patientNoteEntity = new PatientNoteEntity(patientNote.getPatientId());
        patientNoteEntity.setId(patientNote.getId());
        patientNoteEntity.setNoteDetails(patientNote.getNoteDetails().stream().map(PatientNoteMapper::mapToPatientNoteDetailsEntity).collect(Collectors.toList()));
        return patientNoteEntity;
    }

    public static PatientNote.PatientNoteDetails mapToPatientNoteDetailsCore(PatientNoteEntity.PatientNoteDetails patientNoteDetailsEntity){
        if(patientNoteDetailsEntity == null){
            return null;
        }

        PatientNote.PatientNoteDetails patientNoteDetails = new PatientNote.PatientNoteDetails();
        patientNoteDetails.setPatientNoteId(patientNoteDetailsEntity.getPatientNoteId());
        patientNoteDetails.setNote(patientNoteDetailsEntity.getNote());
        patientNoteDetails.setDoctorId(patientNoteDetailsEntity.getDoctorId());
        patientNoteDetails.setDoctorName(patientNoteDetailsEntity.getDoctorName());
        patientNoteDetails.setAddedDateTime(patientNoteDetailsEntity.getAddedDateTime());
        if(patientNoteDetailsEntity.getStatus() != null) {
            patientNoteDetails.setStatus(Status.valueOf(patientNoteDetailsEntity.getStatus().name()));
        }
        if(patientNoteDetailsEntity.getTypeOfProblem() != null) {
            patientNoteDetails.setTypeOfProblem(PatientNote.TypeOfProblem.valueOf(patientNoteDetailsEntity.getTypeOfProblem().name()));
        }

        patientNoteDetails.setDateOfOnSet(patientNoteDetailsEntity.getDateOfOnSet());

        return patientNoteDetails;
    }

    public static PatientNoteEntity.PatientNoteDetails mapToPatientNoteDetailsEntity(PatientNote.PatientNoteDetails patientNoteDetails){
        if(patientNoteDetails == null){
            return null;
        }
        PatientNoteEntity.PatientNoteDetails patientNoteDetailsEntity = new PatientNoteEntity.PatientNoteDetails();
        patientNoteDetailsEntity.setPatientNoteId(patientNoteDetails.getPatientNoteId());
        patientNoteDetailsEntity.setNote(patientNoteDetails.getNote());
        patientNoteDetailsEntity.setDoctorId(patientNoteDetails.getDoctorId());
        patientNoteDetailsEntity.setDoctorName(patientNoteDetails.getDoctorName());
        patientNoteDetailsEntity.setAddedDateTime(patientNoteDetails.getAddedDateTime());
        if(patientNoteDetails.getStatus() != null) {
            patientNoteDetailsEntity.setStatus(com.ilt.cms.api.entity.common.Status.valueOf(patientNoteDetails.getStatus().name()));
        }
        if(patientNoteDetails.getTypeOfProblem() != null) {
            patientNoteDetailsEntity.setTypeOfProblem(PatientNoteEntity.TypeOfProblem.valueOf(patientNoteDetails.getTypeOfProblem().name()));
        }

        patientNoteDetailsEntity.setDateOfOnSet(patientNoteDetails.getDateOfOnSet());
        return patientNoteDetailsEntity;
    }
}
