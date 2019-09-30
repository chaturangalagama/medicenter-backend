package com.ilt.cms.pm.rest.controller.patient.patientVisit;

import com.ilt.cms.api.entity.consultation.PatientReferral;
import com.ilt.cms.downstream.patient.patientVisit.PatientReferralDownstream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;

@RestController
@RequestMapping("/patient-referral")
//@RolesAllowed("ROLE_VIEW_PATIENT_REFERRAL")
public class PatientReferralRestController {

    private static final Logger logger = LoggerFactory.getLogger(PatientReferralRestController.class);
    private PatientReferralDownstream patientReferralDownstream;

    public PatientReferralRestController(PatientReferralDownstream patientReferralDownstream) {
        this.patientReferralDownstream = patientReferralDownstream;
    }

    @PostMapping("/search/{referralId}")
    public ResponseEntity searchPatientReferralById(Principal principal, @PathVariable("referralId") String referralId) {
        logger.info("Getting PatientReferral id:[" + referralId + "] for user [" + principal.getName() + "]");
        return patientReferralDownstream.searchById(referralId);
    }

    @PostMapping("/create")
    @RolesAllowed("ROLE_PATIENT_REFERRAL_MANAGE")
    public ResponseEntity createPatientReferral(Principal principal, @RequestBody PatientReferral referral) {
        logger.info("Creating new PatientReferral for user [" + principal.getName() + "]");
        return patientReferralDownstream.create(referral);
    }

    @PostMapping("/update/{referralId}")
    @RolesAllowed("ROLE_PATIENT_REFERRAL_MANAGE")
    public ResponseEntity updatePatientReferral(Principal principal, @PathVariable("referralId") String referralId,
                                                @RequestBody PatientReferral referral) {
        logger.info("Updating PatientReferral [id]:[" + referralId + "] for user [" + principal.getName() + "]");
        return patientReferralDownstream.update(referralId, referral);
    }
}
