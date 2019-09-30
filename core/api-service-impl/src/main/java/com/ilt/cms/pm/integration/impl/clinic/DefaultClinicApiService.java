package com.ilt.cms.pm.integration.impl.clinic;

import com.ilt.cms.api.entity.clinic.ClinicEntity;
import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.downstream.clinic.ClinicApiService;
import com.ilt.cms.pm.business.service.clinic.ClinicService;
import com.ilt.cms.pm.integration.mapper.clinic.ClinicMapper;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.StatusCode;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

@Service
public class DefaultClinicApiService implements ClinicApiService {
    private ClinicService clinicService;

    public DefaultClinicApiService(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @Override
    public ResponseEntity<ApiResponse> listAll() {
        List<Clinic> result = clinicService.listAll();
        return httpApiResponse(new HttpApiResponse(result));
    }

    @Override
    public ResponseEntity<ApiResponse> searchById(String clinicId) {
        try {
            Clinic result = clinicService.searchById(clinicId);
            return httpApiResponse(new HttpApiResponse(result));
        } catch (CMSException err) {
            return httpApiResponse(new HttpApiResponse(err.getStatusCode(), err.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> addNewClinic(ClinicEntity clinicEntity) {
        try {
            Clinic result = clinicService.addNewClinic(ClinicMapper.mapToCore(clinicEntity));
            return httpApiResponse(new HttpApiResponse(result));
        } catch (CMSException err) {
            return httpApiResponse(new HttpApiResponse(err.getStatusCode(), err.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> modify(String clinicId, ClinicEntity clinicEntity) {
        try {
            Clinic result = clinicService.modify(clinicId, ClinicMapper.mapToCore(clinicEntity));
            return httpApiResponse(new HttpApiResponse(result));
        } catch (CMSException err) {
            return httpApiResponse(new HttpApiResponse(err.getStatusCode(), err.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> remove(String clinicId) {
        clinicService.remove(clinicId);
        return httpApiResponse(new HttpApiResponse(StatusCode.S0000));
    }
}
