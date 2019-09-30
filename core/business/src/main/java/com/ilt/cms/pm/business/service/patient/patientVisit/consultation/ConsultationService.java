package com.ilt.cms.pm.business.service.patient.patientVisit.consultation;

import com.ilt.cms.core.entity.consultation.Consultation;
import com.ilt.cms.database.clinic.ClinicDatabaseService;
import com.ilt.cms.database.patient.patientVisit.consultation.ConsultationDatabaseService;
import com.ilt.cms.database.clinic.DoctorDatabaseService;
import com.ilt.cms.database.patient.PatientDatabaseService;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ConsultationService {

    private static final Logger logger = LoggerFactory.getLogger(ConsultationService.class);
    private ConsultationDatabaseService consultationDatabaseService;
    private ClinicDatabaseService clinicDatabaseService;
    private PatientDatabaseService patientDatabaseService;
    private DoctorDatabaseService doctorDatabaseService;

    public ConsultationService(ConsultationDatabaseService consultationDatabaseService, ClinicDatabaseService clinicDatabaseService, PatientDatabaseService patientDatabaseService, DoctorDatabaseService doctorDatabaseService) {
        this.consultationDatabaseService = consultationDatabaseService;
        this.clinicDatabaseService = clinicDatabaseService;
        this.patientDatabaseService = patientDatabaseService;
        this.doctorDatabaseService = doctorDatabaseService;
    }

    public Consultation searchById(String consultationId) throws CMSException {
        Consultation consultation = consultationDatabaseService.searchById(consultationId);
        if (consultation == null) {
            logger.debug("Consultation not found for [id]:[{}]", consultationId);
            throw new CMSException(StatusCode.E2000);
        } else {
            logger.debug("Consultation found for [id]:[{}] in the system", consultationId);
            return consultation;
        }
    }

    public List<Consultation> searchByPatientId(String patientId) throws CMSException {
        if (!patientDatabaseService.exists(patientId)) {
            List<Consultation> consultations = consultationDatabaseService.findByPatientId(patientId);
            if (consultations != null) {
                logger.debug("Consultations [{}] found for patient [id]:[{}]", consultations.size(), patientId);
                return consultations;
            } else {
                logger.debug("Consultations not found for patient [id]:[{}]", patientId);
                return Collections.emptyList();
            }
        } else {
            throw new CMSException(StatusCode.E2003);
        }
    }

    public Consultation createConsultation(Consultation consultation) throws CMSException {
        checkConsultationValidity(consultation);
        Consultation createdConsultation = consultationDatabaseService.save(consultation);
        logger.debug("Consultation created [id]:[{}]", createdConsultation.getId());
        return createdConsultation;
    }

    public Consultation updateConsultation(String consultationId, Consultation consultation) throws CMSException {

        checkConsultationValidity(consultation);
        Consultation currentConsultation = consultationDatabaseService.searchById(consultationId);
        if (currentConsultation == null) {
            logger.debug("Consultation not found for [id]:[{}]", consultationId);
            throw new CMSException(StatusCode.E2000);
        }
        currentConsultation.copy(consultation);
        Consultation updatedConsultation = consultationDatabaseService.save(currentConsultation);
        logger.debug("ConsultationFollowup updated [id]:[{}]", updatedConsultation.getId());
        return updatedConsultation;
    }

    public void checkConsultationValidity(Consultation consultation) throws CMSException {
        if (!consultation.areParametersValid()) {
            logger.debug("Parameters not valid in Consultation [{}]", consultation);
            throw new CMSException(StatusCode.E1002);
        }
        if (!clinicDatabaseService.exists(consultation.getClinicId())) {
            logger.debug("Clinic not found for id : [{}]", consultation.getClinicId());
            throw new CMSException(StatusCode.E2002);
        }
        if (!patientDatabaseService.exists(consultation.getPatientId())) {
            logger.debug("Clinic not found for id : [{}]", consultation.getPatientId());
            throw new CMSException(StatusCode.E2003);
        }
        if (!doctorDatabaseService.exists(consultation.getDoctorId())) {
            logger.debug("Doctor not found for id : [{}]", consultation.getDoctorId());
            throw new CMSException(StatusCode.E1002, "Couldn't find a doctor for given id");
        }
    }

    public boolean isConsultationExists(String consultationId) throws CMSException {
        if (consultationDatabaseService.exists(consultationId)) {
            return true;
        } else {
            logger.debug("Consultation not found for id : [{}]", consultationId);
            throw new CMSException(StatusCode.E1002, "Couldn't find a Consultation for given id");
        }
    }

}
