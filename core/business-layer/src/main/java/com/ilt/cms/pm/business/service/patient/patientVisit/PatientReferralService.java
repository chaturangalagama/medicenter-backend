package com.ilt.cms.pm.business.service.patient.patientVisit;

import com.ilt.cms.core.entity.consultation.PatientReferral;
import com.ilt.cms.database.patient.patientVisit.PatientReferralDatabaseService;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PatientReferralService {

    private static final Logger logger = LoggerFactory.getLogger(PatientReferralService.class);

    private PatientReferralDatabaseService patientReferralDatabaseService;

    public PatientReferralService(PatientReferralDatabaseService patientReferralDatabaseService) {
        this.patientReferralDatabaseService = patientReferralDatabaseService;
    }

    public PatientReferral searchById(String referralId) throws CMSException {
        PatientReferral referral = patientReferralDatabaseService.searchById(referralId);
        if (referral == null) {
            logger.debug("PatientReferral not found for [id]:[{}]", referralId);
            throw new CMSException(StatusCode.E2000);
        } else {
            logger.debug("PatientReferral found for [id]:[{}] in the system", referral.getId());
            return referral;
        }
    }

    public PatientReferral createReferral(PatientReferral referral) throws CMSException {
        if (!referral.areParametersValid()) {
            logger.debug("Parameters not valid in PatientReferral : [{}]", referral);
            throw new CMSException(StatusCode.E1002);
        } else {
            PatientReferral savedReferral = patientReferralDatabaseService.create(referral);
            logger.debug("PatientReferral created [id]:[{}]", savedReferral.getId());
            return savedReferral;
        }
    }

    public PatientReferral updateReferral(String referralId, PatientReferral referral) throws CMSException {
        if (!referral.areParametersValid()) {
            logger.debug("Parameters not valid in PatientReferral : [{}]", referral);
            throw new CMSException(StatusCode.E1002);
        } else {
            PatientReferral currentReferral = patientReferralDatabaseService.searchById(referralId);
            if (currentReferral == null) {
                logger.debug("PatientReferral not found for [id]:[{}]", referral.getId());
                throw new CMSException(StatusCode.E2000);
            }
            currentReferral.setPatientReferrals(referral.getPatientReferrals());
            PatientReferral savedReferral = patientReferralDatabaseService.create(referral);
            logger.debug("PatientReferral updated [id]:[{}]", savedReferral.getId());
            return savedReferral;
        }
    }

    public void checkPatientReferralValidity(PatientReferral referral) throws CMSException {
        if (!referral.areParametersValid()) {
            logger.debug("Parameters not valid in PatientReferral : [{}]", referral);
            throw new CMSException(StatusCode.E1002);
        }
    }

    public boolean isPatientReferralExists(String referralId) throws CMSException {
        if (patientReferralDatabaseService.exists(referralId)) {
            return true;
        } else {
            logger.debug("PatientReferral not found for id : [{}]", referralId);
            throw new CMSException(StatusCode.E1002, "Couldn't find a PatientReferral for given id");
        }
    }
}
