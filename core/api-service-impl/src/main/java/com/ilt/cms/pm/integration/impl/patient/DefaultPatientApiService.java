package com.ilt.cms.pm.integration.impl.patient;

import com.ilt.cms.api.entity.patient.PatientEntity;
import com.ilt.cms.core.entity.patient.Patient;
import com.ilt.cms.downstream.patient.PatientApiService;
import com.ilt.cms.pm.business.service.patient.PatientService;
import com.ilt.cms.pm.integration.mapper.patient.PatientMapper;
import com.lippo.cms.exception.PatientException;
import com.lippo.commons.util.exception.RestValidationException;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

@Service
public class DefaultPatientApiService implements PatientApiService {
    private static final Logger logger = LoggerFactory.getLogger(DefaultPatientApiService.class);
    private PatientService patientService;

    public DefaultPatientApiService(PatientService patientService) {
        this.patientService = patientService;
    }

    public ResponseEntity<ApiResponse> createPatient(PatientEntity patientEntity) {
        Patient patient = patientService.save(PatientMapper.mapToCore(patientEntity));
        return httpApiResponse(new HttpApiResponse(PatientMapper.mapToEntity(patient)));
    }


    public ResponseEntity<ApiResponse>  search(String type,
                                 String value) {

        Patient patient = null;
        try {
            patient = patientService.findPatient(type, value);
            return httpApiResponse(new HttpApiResponse(PatientMapper.mapToEntity(patient)));
        } catch (PatientException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }


    public ResponseEntity<ApiResponse>  validate(String idNumber) {
        //HttpApiResponse httpResponse = null;
        Boolean vaildate = null;
        try {
            vaildate = patientService.validateIdNumberUse(idNumber);
            return httpApiResponse(new HttpApiResponse(vaildate));
        } catch (PatientException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    public ResponseEntity<ApiResponse>  likeSearch(String value) {
        List<Patient> patientList = patientService.likeSearchPatient(value);
        List<PatientEntity> patientEntityList = new ArrayList<PatientEntity>();
        for (Patient p : patientList) {
            patientEntityList.add(PatientMapper.mapToEntity(p));
        }
        return httpApiResponse(new HttpApiResponse(patientEntityList));
    }

    public ResponseEntity<ApiResponse>  update(String id,
                                 PatientEntity patientEntity) throws RestValidationException {
        Patient patient = null;
        try {
            patient = patientService.updatePatient(id, PatientMapper.mapToCore(patientEntity));
            return httpApiResponse(new HttpApiResponse(PatientMapper.mapToEntity(patient)));
        } catch (PatientException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    public ResponseEntity<ApiResponse>  register(PatientEntity patientEntity) throws RestValidationException {
        Patient patient = null;
        try {
            patient = patientService.patientRegistration(PatientMapper.mapToCore(patientEntity));
            return httpApiResponse(new HttpApiResponse(PatientMapper.mapToEntity(patient)));
        } catch (PatientException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }
}
