package com.ilt.cms.pm.business.service.clinic;

import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.database.clinic.ClinicDatabaseService;
import com.ilt.cms.database.clinic.DoctorDatabaseService;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.StatusCode;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClinicService {

    private static final Logger logger = LoggerFactory.getLogger(ClinicService.class);
    private ClinicDatabaseService clinicDatabaseService;
    private DoctorDatabaseService doctorDatabaseService;

    public ClinicService(ClinicDatabaseService clinicDatabaseService, DoctorDatabaseService doctorDatabaseService) {
        this.clinicDatabaseService = clinicDatabaseService;
        this.doctorDatabaseService = doctorDatabaseService;
    }

    public List<Clinic> listAll() {
        logger.info("Finding all clinics");
        List<Clinic> all = clinicDatabaseService.listAllByIds();
        logger.info("Found [" + all.size() + "] clinics");
        return all;
    }

    public Clinic addNewClinic(Clinic clinic) throws CMSException {
        if (!clinic.areParametersValid()) {
            logger.warn("Invalid parameters [" + clinic.getClinicCode() + "]");
            throw new CMSException(StatusCode.E1002);
        } else {
            boolean codeExists = clinicDatabaseService.checkClinicCodeExists(clinic.getClinicCode());
            if (codeExists) {
                logger.warn("Code already exists, [" + clinic.getClinicCode() + "]");
                throw new CMSException(StatusCode.E1007);
            }
            if (validateDoctorIds(clinic)) {
                throw new CMSException(StatusCode.E2000);
            }

            Clinic savedVersion = clinicDatabaseService.save(clinic);
            return savedVersion;
        }
    }

    public static boolean validateClinicAccess(String username, List<String> userRoles, Clinic clinic, List<String> byPassRole) throws CMSException {
        if (clinic == null) {
            logger.error("Clinic cannot be null");
            return false;
        } else {
            logger.debug("Checking for user [{}] byPassRoles[{}]", username, byPassRole);
            boolean userAllowed = userRoles.stream().anyMatch(byPassRole::contains) ||
                    clinic.getClinicStaffUsernames().stream().anyMatch(s -> s.equals(username));
            if (!userAllowed) {
                String message = "User [" + username + "] not allowed to use this clinic ["
                        + clinic.getId() + "] code[" + clinic.getClinicCode() + "]";
                logger.error(message);
                throw new CMSException(StatusCode.E1002, message);
            }
        }
        return true;
    }

    private boolean validateDoctorIds(Clinic clinic) {
        if (clinic.getAttendingDoctorId() != null) {
            for (String doctorId : clinic.getAttendingDoctorId()) {
                boolean exists = doctorDatabaseService.exists(doctorId);
                if (!exists) {
                    return true;
                }
            }
        }
        return false;
    }

    public Clinic searchById(String clinicId) throws CMSException {
        Optional<Clinic> clinicOpt = clinicDatabaseService.findOne(clinicId);
        if (!clinicOpt.isPresent()) {
            throw new CMSException(StatusCode.E2000);
        } else {
            return clinicOpt.get();
        }
    }
    public Map<String, Clinic> searchById(List<String> clinicIds) {
        return clinicDatabaseService.listAllByIds(clinicIds)
                .stream()
                .collect(Collectors.toMap(PersistedObject::getId, clinic -> clinic));
    }

    public Clinic modify(String clinicId, Clinic clinic) throws CMSException {
        logger.info("modifying clinic [" + clinicId + "]");
        Optional<Clinic> currentClinicOpt = clinicDatabaseService.findOne(clinicId);
        if (!currentClinicOpt.isPresent()) {
            throw new CMSException(StatusCode.E2000);
        } else {
            Clinic currentClinic = currentClinicOpt.get();
            logger.debug("clinic found validating the doctor list [" + clinic.getAttendingDoctorId()
                    + "] id [" + currentClinic.getId()
                    + "] current Clinic[" + currentClinic + "]");
            currentClinic.copy(clinic);
            logger.debug("Validation success, persisting the clinic [" + currentClinic + "] id [" + currentClinic.getId() + "]");
            Clinic savedClinic = clinicDatabaseService.save(currentClinic);
            return savedClinic;
        }
    }

    public boolean remove(String clinicId) {
        return clinicDatabaseService.delete(clinicId);
    }
}
