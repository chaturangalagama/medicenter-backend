package com.ilt.cms.pm.business.service.clinic;

import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.consultation.ConsultationTemplate;
import com.ilt.cms.core.entity.doctor.Doctor;
import com.ilt.cms.database.clinic.ClinicDatabaseService;
import com.ilt.cms.database.clinic.DoctorDatabaseService;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.CommonUtils;
import com.lippo.commons.util.StatusCode;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private static final Logger logger = LoggerFactory.getLogger(DoctorService.class);

    private DoctorDatabaseService doctorDatabaseService;
    private ClinicDatabaseService clinicDatabaseService;

    public DoctorService(DoctorDatabaseService doctorDatabaseService, ClinicDatabaseService clinicDatabaseService) {
        this.doctorDatabaseService = doctorDatabaseService;
        this.clinicDatabaseService = clinicDatabaseService;
    }


    public List<Doctor> listAll() {
        List<Doctor> all = doctorDatabaseService.findAll();
        logger.info("found [" + all.size() + "] doctors in the system");
        return all;
    }

    public Doctor searchById(String doctorId) throws CMSException {
        Optional<Doctor> doctorOpt = doctorDatabaseService.findOne(doctorId);
        if (!doctorOpt.isPresent()) {
            //return new CmsServiceResponse<>(StatusCode.E2000);
            throw new CMSException(StatusCode.E2000);
        } else {
            return doctorOpt.get();
        }
    }

    public Doctor addNewDoctor(Doctor doctor) throws NoSuchFieldException, IllegalAccessException, CMSException {
        Doctor savedDoctor;
        boolean parameterValid = doctor.areParameterValid();
        if (!parameterValid) {
            logger.error("Parameters not valid [" + doctor + "]");
            //return new CmsServiceResponse<>(StatusCode.E1002);
            throw new CMSException(StatusCode.E1002);
        } else {
            populateTemplateId(doctor);
            savedDoctor = doctorDatabaseService.save(doctor);
        }
        return savedDoctor;
    }

    private void populateTemplateId(Doctor doctor) throws NoSuchFieldException, IllegalAccessException {
        Field id = PersistedObject.class.getDeclaredField("id");
        id.setAccessible(true);
        for (ConsultationTemplate consultationTemplate : doctor.getConsultationTemplates()) {
            id.set(consultationTemplate, CommonUtils.idGenerator());
        }
    }

    public Doctor modify(String doctorId, Doctor doctor) throws NoSuchFieldException, IllegalAccessException, CMSException {
        Optional<Doctor> currentDoctorOpt = doctorDatabaseService.findOne(doctorId);
        if (!currentDoctorOpt.isPresent()) {
            //return new CmsServiceResponse<>(StatusCode.E2000);
            throw new CMSException(StatusCode.E2000);
        } else {
            boolean parameterValid = doctor.areParameterValid();
            if (!parameterValid) {
                logger.error("Parameters not valid [" + doctor + "]");
                //return new CmsServiceResponse<>(StatusCode.E1002);
                throw new CMSException(StatusCode.E1002);
            } else {
                populateTemplateId(doctor);
                Doctor currentDoctor = currentDoctorOpt.get();
                currentDoctor.copy(doctor);
                Doctor savedDoctor = doctorDatabaseService.save(currentDoctor);
                return savedDoctor;
            }
        }
    }

    public List<Doctor> listAll(String clinicId) throws CMSException {
        logger.info("finding clinic [" + clinicId + "]");
        Optional<Clinic> clinicOpt = clinicDatabaseService.findOne(clinicId);
        if (!clinicOpt.isPresent()) {
            //return new CmsServiceResponse<>(StatusCode.E1005, "Clinic ID not valid");
            throw new CMSException(StatusCode.E1005, "Clinic ID not valid");
        }
        List<Doctor> allDoctors = doctorDatabaseService.findByStatus(Status.ACTIVE);
        List<Doctor> clinicDoctors = allDoctors.stream()
                .filter(doctor -> clinicOpt.get().getAttendingDoctorId().contains(doctor.getId()))
                .collect(Collectors.toList());
        return clinicDoctors;
    }

    public List<Doctor> findAll() {
        return doctorDatabaseService.findAll();
    }
}
