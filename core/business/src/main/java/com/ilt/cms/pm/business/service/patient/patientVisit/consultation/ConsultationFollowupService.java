package com.ilt.cms.pm.business.service.patient.patientVisit.consultation;

import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.core.entity.visit.ConsultationFollowup;
import com.ilt.cms.database.clinic.ClinicDatabaseService;
import com.ilt.cms.database.patient.patientVisit.consultation.ConsultationFollowupDatabaseService;
import com.ilt.cms.database.clinic.DoctorDatabaseService;
import com.ilt.cms.database.patient.PatientDatabaseService;
import com.ilt.cms.pm.business.service.clinic.ClinicService;
import com.lippo.cms.exception.CMSException;
import com.lippo.cms.util.UserInfoHelper;
import com.lippo.commons.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@Service
public class ConsultationFollowupService {

    private static final Logger logger = LoggerFactory.getLogger(ConsultationFollowupService.class);
    private ConsultationFollowupDatabaseService consultationFollowupDatabaseService;
    private ClinicDatabaseService clinicDatabaseService;
    private DoctorDatabaseService doctorDatabaseService;
    private PatientDatabaseService patientDatabaseService;

    public ConsultationFollowupService(ConsultationFollowupDatabaseService consultationFollowupDatabaseService,
                                       ClinicDatabaseService clinicDatabaseService,
                                       DoctorDatabaseService doctorDatabaseService,
                                       PatientDatabaseService patientDatabaseService) {
        this.consultationFollowupDatabaseService = consultationFollowupDatabaseService;
        this.clinicDatabaseService = clinicDatabaseService;
        this.doctorDatabaseService = doctorDatabaseService;
        this.patientDatabaseService = patientDatabaseService;
    }

    public ConsultationFollowup searchById(String followUpId) throws CMSException {
        ConsultationFollowup followup = consultationFollowupDatabaseService.searchById(followUpId);
        if (followup == null) {
            logger.debug("ConsultationFollowup not found for [id]:[{}]", followUpId);
            throw new CMSException(StatusCode.E2000);
        } else {
            logger.debug("ConsultationFollowup found for [id]:[{}] in the system", followUpId);
            return followup;
        }
    }

    public ConsultationFollowup createFollowup(ConsultationFollowup followup) throws CMSException {
        checkConsultationFollowupValidity(followup);
        ConsultationFollowup createdFollowUp = consultationFollowupDatabaseService.save(followup);
        logger.debug("ConsultationFollowup created [id]:[{id}]");
        return createdFollowUp;
    }

    public ConsultationFollowup updateFollowup(String followUpId, ConsultationFollowup followup) throws CMSException {
        checkConsultationFollowupValidity(followup);
        ConsultationFollowup currentFollowup = consultationFollowupDatabaseService.searchById(followUpId);
        if (currentFollowup == null) {
            logger.debug("ConsultationFollowup not found for [id]:[{}]", followup.getId());
            throw new CMSException(StatusCode.E2000);
        }
        currentFollowup.copyParameters(followup);
        ConsultationFollowup updatedFollowup = consultationFollowupDatabaseService.save(followup);
        logger.debug("ConsultationFollowup updated [id]:[{}]", updatedFollowup.getId());
        return updatedFollowup;
    }

    public void checkConsultationFollowupValidity(ConsultationFollowup followup) throws CMSException {
        if (!followup.areParametersValid()) {
            logger.debug("Parameters not valid in ConsultationFollowup [{}]", followup);
            throw new CMSException(StatusCode.E1002);
        }
        if (!clinicDatabaseService.exists(followup.getClinicId())) {
            logger.debug("Clinic not found for id : [{}]", followup.getClinicId());
            throw new CMSException(StatusCode.E2002);
        }
        if (!patientDatabaseService.exists(followup.getPatientId())) {
            logger.debug("Clinic not found for id : [{}]", followup.getPatientId());
            throw new CMSException(StatusCode.E2003);
        }
        if (!doctorDatabaseService.exists(followup.getDoctorId())) {
            logger.debug("Doctor not found for id : [{}]", followup.getDoctorId());
            throw new CMSException(StatusCode.E1002, "Couldn't find a doctor for given id");
        }
    }

    public boolean isConsultationFollowupExists(String followupId) throws CMSException {
        if (consultationFollowupDatabaseService.exists(followupId)) {
            return true;
        } else {
            logger.debug("ConsultationFollowup not found for id : [{}]", followupId);
            throw new CMSException(StatusCode.E1002, "Couldn't find a ConsultationFollowup for given id");
        }
    }

    public List<ConsultationFollowup> listFollowups(Principal principal, String clinicId, LocalDate startDate, LocalDate endDate) throws CMSException {
        String loginName = UserInfoHelper.loginName(principal);
        List<String> userRoles = UserInfoHelper.getUserRoles(principal);

        if(loginName == null){
            throw new CMSException(StatusCode.E1002, "No login name");
        }
        if(userRoles == null){
            throw new CMSException(StatusCode.E2000, "No user Role");
        }
        Optional<Clinic> clinicOpt = clinicDatabaseService.findOne(clinicId);
        if(!clinicOpt.isPresent()){
            throw new CMSException(StatusCode.E2000, "Clinic not found");
        }
        Clinic clinic = clinicOpt.get();

        if(!ClinicService.validateClinicAccess(loginName, userRoles, clinic, Collections.emptyList())){
            logger.error("User [" + loginName + "] has no access to clinic for clinic revenue report ["
                    + (clinic != null ? clinic.getClinicCode() : clinicId)
                    + "] id [" + clinicId + "]");
            throw new CMSException(StatusCode.E1010, "Not allowed to view clinic details");
        }

        return consultationFollowupDatabaseService.findByClinicIdAndFollowupDateBetween(clinicId, startDate, endDate,
                new Sort(Sort.Direction.DESC, "followupDate"));
    }
}
