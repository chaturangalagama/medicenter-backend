package com.ilt.cms.pm.business.service.patient;

import com.ilt.cms.core.entity.patient.MedicalAlert;
import com.ilt.cms.core.entity.patient.Patient;
import com.ilt.cms.core.entity.patient.PatientAllergy;
import com.ilt.cms.database.patient.MedicalAlertDatabaseService;
import com.ilt.cms.database.patient.PatientDatabaseService;
import com.lippo.cms.exception.MedicalAlertException;
import com.lippo.commons.util.CommonUtils;
import com.lippo.commons.util.StatusCode;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MedicalAlertService {

    private static final Logger logger = LoggerFactory.getLogger(MedicalAlertService.class);

    private PatientDatabaseService patientDatabaseService;
    private MedicalAlertDatabaseService medicalAlertDatabaseService;

    public MedicalAlertService(PatientDatabaseService patientDatabaseService, MedicalAlertDatabaseService medicalAlertDatabaseService) {
        this.patientDatabaseService = patientDatabaseService;
        this.medicalAlertDatabaseService = medicalAlertDatabaseService;
    }

    public MedicalAlert medicalAlertList(String patientId) throws MedicalAlertException {
        logger.info("finding alerts for [" + patientId + "]");
        MedicalAlert medicalAlert = medicalAlertDatabaseService.findByPatientId(patientId);
        if (medicalAlert == null) {
            logger.info("there were no predefined alerts");
            medicalAlert = new MedicalAlert(patientId);
        }
        logger.info("loading allergies of patient into vaccination");
        Optional<Patient> patientOpt = patientDatabaseService.findPatientById(patientId);
        if (!patientOpt.isPresent()) {
            //return new CmsServiceResponse<>(StatusCode.E2000);
            throw new MedicalAlertException(StatusCode.E2000);

        }
        populateAllergies(medicalAlert, patientOpt.get());
        return medicalAlert;
    }

    private void populateAllergies(MedicalAlert medicalAlert, Patient patient) {
        List<PatientAllergy> allergies = patient.getAllergies();
        for (PatientAllergy allergy : allergies) {
            medicalAlert.addAlert(MedicalAlert.AlertType.ALLERGY, allergy.getName(),
                    "[" + allergy.getAllergyType() + "] Remark [" + allergy.getRemarks() + "]", MedicalAlert.MedicalAlertDetails.Priority.HIGH,
                    allergy.getAddedDate(), null, false);
        }
        medicalAlert.getDetails().sort(Comparator.comparing(o -> o.getPriority().toString()));
    }

    public MedicalAlert add(String patientId, List<MedicalAlert.MedicalAlertDetails> medicalAlertDetails) throws MedicalAlertException {
        logger.info("adding a new medical alert for [" + patientId + "] alert [" + medicalAlertDetails + "]");
        Optional<Patient> patientOpt = patientDatabaseService.findPatientById(patientId);
        if (!patientOpt.isPresent()) {
            logger.error("Patient dose not exists [" + patientId + "]");
            //return new CmsServiceResponse<>(StatusCode.E2000);
            throw new MedicalAlertException(StatusCode.E2000);
        } else {
            MedicalAlert medicalAlert = medicalAlertDatabaseService.findByPatientId(patientId);
            for (MedicalAlert.MedicalAlertDetails medicalAlertDetail : medicalAlertDetails) {
                if (medicalAlert == null) {
                    medicalAlert = new MedicalAlert(patientId);
                }
                boolean valid = medicalAlertDetail.areParametersValid();
                if (!valid) {
                    logger.error("Error in adding alert invalid parameters [" + medicalAlertDetails + "]");
                    //return new CmsServiceResponse<>(StatusCode.E1002);
                    throw new MedicalAlertException(StatusCode.E1002);
                }
                medicalAlertDetail.resetAddedDate();
                medicalAlertDetail.setAlertId(CommonUtils.idGenerator());
                medicalAlert.addAlert(medicalAlertDetail);
            }
            MedicalAlert savedVersion = medicalAlertDatabaseService.save(medicalAlert);

            populateAllergies(savedVersion, patientOpt.get()); // load rest of the allergies
            return savedVersion;
        }
    }


    public boolean delete(List<String> medicalAlertId) throws MedicalAlertException {
        logger.info("Deleting medical alert [" + medicalAlertId + "]");
        if (medicalAlertId.size() == 0) {
            logger.error("There should be atlest one item size [" + medicalAlertId.size() + "]");
            //return new CmsServiceResponse<>(StatusCode.E1005, "There should be atlest one item");
            throw new MedicalAlertException(StatusCode.E1005, "There should be atlest one item");
        }
        MedicalAlert medicalAlert = medicalAlertDatabaseService.findByMedicalAlertId(medicalAlertId.get(0));
        if (medicalAlert == null) {
            logger.error("Error in adding alert id not valid [" + medicalAlertId + "]");
            //return new CmsServiceResponse<>(StatusCode.E1002);
            throw new MedicalAlertException(StatusCode.E1002);
        }

        List<MedicalAlert.MedicalAlertDetails> newList = medicalAlert.getDetails().stream()
                .filter(mad -> mad.getAlertId() != null && !medicalAlertId.contains(mad.getAlertId()))
                .collect(Collectors.toList());

        medicalAlert.getDetails().clear();
        medicalAlert.getDetails().addAll(newList);
        MedicalAlert savedVersion = medicalAlertDatabaseService.save(medicalAlert);
        //return new CmsServiceResponse<>(StatusCode.S0000);
        return true;
    }
}
