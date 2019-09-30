package com.ilt.cms.pm.integration.impl.clinic;

import com.ilt.cms.api.entity.doctor.DoctorEntity;
import com.ilt.cms.core.entity.doctor.Doctor;
import com.ilt.cms.downstream.clinic.DoctorApiService;
import com.ilt.cms.pm.business.service.clinic.DoctorService;
import com.ilt.cms.pm.integration.mapper.clinic.DoctorMapper;
import com.lippo.cms.exception.CMSException;
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
public class DefaultDoctorApiService implements DoctorApiService {
    private static final Logger logger = LoggerFactory.getLogger(DefaultDoctorApiService.class);
    private DoctorService doctorService;

    public DefaultDoctorApiService(DoctorService doctorService){
        this.doctorService = doctorService;
    }
    @Override
    public ResponseEntity<ApiResponse> listAll() {

        List<Doctor> doctors = doctorService.listAll();
        List<DoctorEntity> doctorEntities = new ArrayList<>();
        doctors.stream().forEach(p -> doctorEntities.add(DoctorMapper.mapToEntity(p)));
        return httpApiResponse(new HttpApiResponse(doctorEntities));

    }

    @Override
    public ResponseEntity<ApiResponse> listAll(String clinicId) {
        try {
            List<Doctor> doctors = doctorService.listAll(clinicId);
            List<DoctorEntity> doctorEntities = new ArrayList<>();
            doctors.stream().forEach(p -> doctorEntities.add(DoctorMapper.mapToEntity(p)));
            return httpApiResponse(new HttpApiResponse(doctorEntities));
        } catch (CMSException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> searchById(String clinicId) {
        try {
            Doctor doctor = doctorService.searchById(clinicId);
            return httpApiResponse(new HttpApiResponse(DoctorMapper.mapToEntity(doctor)));
        } catch (CMSException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }

    }

    @Override
    public ResponseEntity<ApiResponse> addNewDoctor(DoctorEntity doctorEntity) throws IllegalAccessException, NoSuchFieldException {
        try {
            Doctor newDoctor = doctorService.addNewDoctor(DoctorMapper.mapToCore(doctorEntity));
            return httpApiResponse(new HttpApiResponse(DoctorMapper.mapToEntity(newDoctor)));
        } catch (NoSuchFieldException e) {
            throw e;
        } catch (IllegalAccessException e) {
            throw e;
        } catch (CMSException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> modify(String clinicId, DoctorEntity doctorEntity) throws IllegalAccessException, NoSuchFieldException {
        try {
            Doctor doctor = doctorService.modify(clinicId, DoctorMapper.mapToCore(doctorEntity));
            return httpApiResponse(new HttpApiResponse(DoctorMapper.mapToEntity(doctor)));
        } catch (NoSuchFieldException e) {
            throw e;
        } catch (IllegalAccessException e) {
            throw e;
        } catch (CMSException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }
}
