package com.ilt.cms.downstream;

import com.ilt.cms.api.entity.doctor.DoctorEntity;
import com.lippo.commons.web.api.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface DoctorDownstream {
    ResponseEntity<ApiResponse> listAll();

    ResponseEntity<ApiResponse> listAll(String clinicId);

    ResponseEntity<ApiResponse> searchById(String clinicId);

    ResponseEntity<ApiResponse> addNewDoctor(DoctorEntity doctorEntity) throws IllegalAccessException, NoSuchFieldException;

    ResponseEntity<ApiResponse> modify(String clinicId, DoctorEntity doctorEntity) throws IllegalAccessException, NoSuchFieldException;
}