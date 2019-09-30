package com.ilt.cms.pm.business.service.patient.patientVisit.consultation;

import com.ilt.cms.core.entity.CmsConstant;
import com.ilt.cms.core.entity.consultation.ConsultationTemplate;
import com.ilt.cms.core.entity.doctor.Doctor;
import com.ilt.cms.core.entity.patient.Patient;
import com.ilt.cms.database.patient.patientVisit.consultation.ConsultationTemplateDatabaseService;
import com.ilt.cms.database.clinic.DoctorDatabaseService;
import com.ilt.cms.database.patient.PatientDatabaseService;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.StatusCode;
import com.mitchellbosecke.pebble.error.PebbleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.StringLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultationTemplateService {
    private static final Logger logger = LoggerFactory.getLogger(ConsultationTemplateService.class);
    public static final String GLOBAL = "GLOBAL";

    private ConsultationTemplateDatabaseService consultationTemplateDatabaseService;
    private DoctorDatabaseService doctorDatabaseService;
    private PatientDatabaseService patientDatabaseService;

    private final PebbleEngine pebbleEngine;


    public ConsultationTemplateService(ConsultationTemplateDatabaseService consultationTemplateDatabaseService, DoctorDatabaseService doctorDatabaseService, PatientDatabaseService patientDatabaseService) {
        this.consultationTemplateDatabaseService = consultationTemplateDatabaseService;
        this.doctorDatabaseService = doctorDatabaseService;
        this.patientDatabaseService = patientDatabaseService;

        this.pebbleEngine = new PebbleEngine.Builder().loader(new StringLoader()).build();
    }

    /*public class ConsultationTemplateList {
        private List<ConsultationTemplate> templates;

        public ConsultationTemplateList(List<ConsultationTemplate> templates) {
            this.templates = templates;
        }

        public List<ConsultationTemplate> getTemplates() {
            return templates;
        }
    }*/

    private int calculateAge(LocalDate birthDate, LocalDate currentDate) {
        if ((birthDate != null) && (currentDate != null)) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            return 0;
        }
    }

    public String loadTemplate(String templateType, String templateId, String doctorId, String patientId) throws CMSException, IOException, PebbleException {

        logger.info("loading template [" + templateType + "] [" + templateId + "] patientId[" + patientId + "] ");

        Optional<Doctor> doctorOpt = doctorDatabaseService.findOne(doctorId);
        if (!doctorOpt.isPresent()) {
            logger.error("doctor id [" + doctorId + "] not found");
            throw new CMSException(StatusCode.E1002, "Doctor not found");
        }
        ConsultationTemplate consultationTemplate;
        if (templateType.equals(GLOBAL)) {
            consultationTemplate = consultationTemplateDatabaseService.findOne(templateId);
            if (consultationTemplate == null) {
                logger.error("Template id [" + templateId + "] not found");
                throw new CMSException(StatusCode.E1002, "Template not found");
            }
        } else {
            Optional<ConsultationTemplate> template = doctorOpt.get().getConsultationTemplates().stream()
                    .filter(ct -> ct.getId().equals(templateId))
                    .findFirst();
            if (template.isPresent()) {
                consultationTemplate = template.get();
            } else {
                logger.error("Template id [" + templateId + "] not found");
                throw new CMSException(StatusCode.E1002, "Template not found");
            }
        }
        Optional<Patient> patientOpt = patientDatabaseService.findPatientById(patientId);
        if (!patientOpt.isPresent()) {
            logger.error("patient id [" + patientId + "] not found");
            throw new CMSException(StatusCode.E1002, "Patient not found");
        }
        String template = loadTemplate(consultationTemplate, patientOpt.get(), doctorOpt.get());
        return template;
    }

    private String loadTemplate(ConsultationTemplate consultationTemplate, Patient patient, Doctor doctor) throws IOException, PebbleException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("doctor_name", doctor.getName());
        map.put("doctor_education", doctor.getEducation());
        map.put("patient_name", patient.getName());
        map.put("patient_id", patient.getUserId().getNumber());
        map.put("patient_age", calculateAge(patient.getDob(), LocalDate.now()));
        map.put("patient_gender", patient.getGender().toString());
        map.put("patient_email", patient.getEmailAddress());
        map.put("patient_number", patient.getPatientNumber());
        map.put("patient_contact", patient.getContactNumber().getNumber());
        map.put("patient_address", patient.getAddress());
        map.put("today_date", new SimpleDateFormat(CmsConstant.JSON_DATE_FORMAT).format(System.currentTimeMillis()));

        StringWriter writer = new StringWriter();
        PebbleTemplate pebbleTemplate = pebbleEngine.getTemplate(consultationTemplate.getTemplate());
        pebbleTemplate.evaluate(writer, map);
        return writer.toString();
    }

    public List<ConsultationTemplate> listTemplate(String doctorId) throws CMSException {
        logger.info("Loading templates [" + doctorId + "]");
        Optional<Doctor> doctorOpt = doctorDatabaseService.findOne(doctorId);
        if (!doctorOpt.isPresent()) {
            throw new CMSException(StatusCode.E1001, "Doctor not found");
        }
        Doctor doctor = doctorOpt.get();
        List<ConsultationTemplate> globalTemplates = consultationTemplateDatabaseService.findAll();
        ArrayList<ConsultationTemplate> templates = new ArrayList<>();
        for (ConsultationTemplate globalTemplate : globalTemplates) {
            globalTemplate.setType(GLOBAL);
            templates.add(globalTemplate);
        }
        if (doctor.getConsultationTemplates() != null) {
            for (ConsultationTemplate consultationTemplate : doctor.getConsultationTemplates()) {
                consultationTemplate.setType("DOCTOR");
                templates.add(consultationTemplate);
            }
        }
        return templates;
    }
}
