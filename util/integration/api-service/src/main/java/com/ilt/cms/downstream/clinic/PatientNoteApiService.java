package com.ilt.cms.downstream.clinic;


import com.ilt.cms.api.entity.common.Status;
import com.ilt.cms.api.entity.patient.PatientNoteEntity;
import com.lippo.commons.web.api.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface PatientNoteApiService {
    ResponseEntity<ApiResponse> patientNote(String patientId);

    ResponseEntity<ApiResponse> changeNoteDetailState(String patientId, String patientNoteDetailId, Status status);

    ResponseEntity<ApiResponse> addNewNote(String patientId, PatientNoteEntity.PatientNoteDetails patientNoteDetails);
}