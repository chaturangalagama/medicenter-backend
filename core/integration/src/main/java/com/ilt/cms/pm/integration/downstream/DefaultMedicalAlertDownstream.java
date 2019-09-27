package com.ilt.cms.pm.integration.downstream;

import com.ilt.cms.api.entity.patient.MedicalAlertEntity;
import com.ilt.cms.core.entity.patient.MedicalAlert;
import com.ilt.cms.downstream.MedicalAlertDownstream;
import com.ilt.cms.pm.business.service.patient.MedicalAlertService;
import com.ilt.cms.pm.integration.mapper.MedicalAlertMapper;
import com.lippo.cms.exception.MedicalAlertException;
import com.lippo.commons.util.StatusCode;
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
public class DefaultMedicalAlertDownstream implements MedicalAlertDownstream {
    private static final Logger logger = LoggerFactory.getLogger(DefaultMedicalAlertDownstream.class);
    private MedicalAlertService medicalAlertService;

    public DefaultMedicalAlertDownstream(MedicalAlertService medicalAlertService){
        this.medicalAlertService = medicalAlertService;
    }

    @Override
    public ResponseEntity<ApiResponse> medicalAlertList(String patientId) {
        logger.info("Retrieving medical alerts for [" + patientId + "]");
        MedicalAlert medicalAlert = null;
        try {
            medicalAlert = medicalAlertService.medicalAlertList(patientId);
            return httpApiResponse(new HttpApiResponse(MedicalAlertMapper.mapToEntity(medicalAlert)));
        } catch (MedicalAlertException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }

    }

    @Override
    public ResponseEntity<ApiResponse> add(String patientId, List<MedicalAlertEntity.MedicalAlertDetails> details) {
        logger.info("Retrieving medical alerts for [" + patientId + "]");
        List<MedicalAlert.MedicalAlertDetails> medicalAlertDetailsList = new ArrayList<>();
        details.stream().forEach(d->medicalAlertDetailsList.add(MedicalAlertMapper.mapToCore(d)));

        try {
            MedicalAlert medicalAlert = medicalAlertService.add(patientId, medicalAlertDetailsList);
            return httpApiResponse(new HttpApiResponse(MedicalAlertMapper.mapToEntity(medicalAlert)));
        } catch (MedicalAlertException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> delete(List<String> medicalAlertId) {
        logger.info("deleting medical alert id [" + medicalAlertId + "]");

        try {
            boolean deleteSuccess = medicalAlertService.delete(medicalAlertId);
            return httpApiResponse(new HttpApiResponse(StatusCode.S0000));
        } catch (MedicalAlertException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }
}