package com.ilt.cms.pm.integration.impl.patient.patientVisit;

import com.ilt.cms.core.entity.consultation.PatientReferral;
import com.ilt.cms.downstream.patient.patientVisit.PatientReferralApiService;
import com.ilt.cms.pm.business.service.patient.patientVisit.PatientReferralService;
import com.ilt.cms.pm.integration.mapper.patient.patientVisit.PatientReferralMapper;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

@Service
public class DefaultPatientReferralApiService implements PatientReferralApiService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultPatientReferralApiService.class);
    private PatientReferralService patientReferralService;

    public DefaultPatientReferralApiService(PatientReferralService patientReferralService) {
        this.patientReferralService = patientReferralService;
    }


    @Override
    public ResponseEntity<ApiResponse> searchById(String referralId) {
        try {
            PatientReferral referral = patientReferralService.searchById(referralId);
            logger.info("PatientReferral found for [id]:[" + referralId + "]");
            return httpApiResponse(new HttpApiResponse(PatientReferralMapper.mapPatientReferralToEntity(referral)));
        } catch (CMSException e) {
            logger.error("PatientReferral search error [" + e.getStatusCode() + "]: " + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> create(com.ilt.cms.api.entity.consultation.PatientReferral patientReferral) {
        try {
            PatientReferral createdReferral = patientReferralService.createReferral(PatientReferralMapper.mapPatientReferralToCore(patientReferral));
            logger.info("PatientReferral created [id]:[" + createdReferral.getId() + "]");
            return httpApiResponse(new HttpApiResponse(PatientReferralMapper.mapPatientReferralToEntity(createdReferral)));
        } catch (CMSException e) {
            logger.error("PatientReferral create error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> update(String referralId, com.ilt.cms.api.entity.consultation.PatientReferral patientReferral) {
        try {
            PatientReferral referral = patientReferralService.updateReferral(referralId, PatientReferralMapper.mapPatientReferralToCore(patientReferral));
            logger.info("PatientReferral updated [id]:[" + referral.getId() + "]");
            return httpApiResponse(new HttpApiResponse(PatientReferralMapper.mapPatientReferralToEntity(referral)));
        } catch (CMSException e) {
            logger.error("PatientReferral updating error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }
}
