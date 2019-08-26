package com.ilt.cms.downstream;

import com.ilt.cms.api.entity.clinic.ClinicEntity;
import com.lippo.commons.web.api.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface ClinicDownstream {
    ResponseEntity<ApiResponse> listAll();

    ResponseEntity<ApiResponse> searchById(String clinicId);

    ResponseEntity<ApiResponse> addNewClinic(ClinicEntity clinicEntity);

    ResponseEntity<ApiResponse> modify(String clinicId, ClinicEntity clinicEntity);

    ResponseEntity<ApiResponse> remove(String clinicId);
}