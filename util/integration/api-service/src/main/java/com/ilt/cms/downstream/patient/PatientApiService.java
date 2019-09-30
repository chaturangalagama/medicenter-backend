package com.ilt.cms.downstream.patient;

import com.ilt.cms.api.entity.patient.PatientEntity;
import com.lippo.commons.util.exception.RestValidationException;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

public interface PatientApiService {
    ResponseEntity<ApiResponse> createPatient(PatientEntity patientEntity);

    public ResponseEntity<ApiResponse>  search(String type,
                                               String value);


    public ResponseEntity<ApiResponse>  validate(String idNumber);

    public ResponseEntity<ApiResponse>  likeSearch(String value);

    public ResponseEntity<ApiResponse>  update(String id,
                                               PatientEntity patientEntity) throws RestValidationException;

    public ResponseEntity<ApiResponse>  register(PatientEntity patientEntity) throws RestValidationException;


}
