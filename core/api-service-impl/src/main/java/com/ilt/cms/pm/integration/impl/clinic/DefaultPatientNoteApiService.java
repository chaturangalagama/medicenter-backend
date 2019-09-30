package com.ilt.cms.pm.integration.impl.clinic;

import com.ilt.cms.api.entity.common.Status;
import com.ilt.cms.api.entity.patient.PatientNoteEntity;
import com.ilt.cms.core.entity.patient.PatientNote;
import com.ilt.cms.downstream.clinic.PatientNoteApiService;
import com.ilt.cms.pm.business.service.clinic.PatientNoteService;
import com.ilt.cms.pm.integration.mapper.clinic.PatientNoteMapper;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

@Service
public class DefaultPatientNoteApiService implements PatientNoteApiService {
    private static final Logger logger = LoggerFactory.getLogger(DefaultPatientNoteApiService.class);
    private PatientNoteService patientNoteService;

    public DefaultPatientNoteApiService(PatientNoteService patientNoteService){
        this.patientNoteService = patientNoteService;
    }
    @Override
    public ResponseEntity<ApiResponse> patientNote(String patientId) {
        PatientNote patientNote = patientNoteService.patientNote(patientId);
        return httpApiResponse(new HttpApiResponse(PatientNoteMapper.mapToEntity(patientNote)));
    }

    @Override
    public ResponseEntity<ApiResponse> changeNoteDetailState(String patientId, String patientNoteDetailId, Status status) {
        PatientNote patientNote = patientNoteService.changeNoteDetailState(patientId, patientNoteDetailId, com.ilt.cms.core.entity.Status.valueOf(status.name()));
        return httpApiResponse(new HttpApiResponse(PatientNoteMapper.mapToEntity(patientNote)));
    }

    @Override
    public ResponseEntity<ApiResponse> addNewNote(String patientId, PatientNoteEntity.PatientNoteDetails patientNoteDetails) {
        try {
            PatientNote patientNote = patientNoteService.addNewNote(patientId, PatientNoteMapper.mapToPatientNoteDetailsCore(patientNoteDetails));
            return httpApiResponse(new HttpApiResponse(PatientNoteMapper.mapToEntity(patientNote)));
        } catch (CMSException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }
}
