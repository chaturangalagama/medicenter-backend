package com.ilt.cms.pm.business.service.patient;

import com.ilt.cms.core.entity.vital.Vital;
import com.ilt.cms.core.entity.vital.VitalMaster;
import com.ilt.cms.repository.patient.PatientRepository;
import com.ilt.cms.repository.patient.VitalRepository;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VitalService {

    private static final Logger logger = LoggerFactory.getLogger(VitalService.class);
    private PatientRepository patientRepository;
    private VitalRepository vitalRepository;

    private static final VitalMaster weight = new VitalMaster("weight", "Weight", "Kg");
    private static final VitalMaster height = new VitalMaster("height", "Height", "cm");
    public static final VitalMaster bmi = new VitalMaster("bmi", "BMI", "kg/m2");

    public static final List<VitalMaster> defaultMasters = Arrays.asList(weight, height,
            new VitalMaster("temperature", "Temperature", "C"),
            new VitalMaster("pulse", "Pulse", "BP/m"),
            new VitalMaster("systolic", "Systolic", "mmHg"),
            new VitalMaster("diastolic", "Diastolic", "mmHg"));

    public static final List<VitalMaster> extraMasters = Arrays.asList(bmi,
            new VitalMaster("respiration", "Respiration", "BPm"),
            new VitalMaster("sa02", "SaO2", "%"));

    public static List<VitalMaster> vitalMasters;

    static {
        vitalMasters = new ArrayList<>(defaultMasters);
        vitalMasters.addAll(extraMasters);
    }

    public VitalService(PatientRepository patientRepository, VitalRepository vitalRepository) {
        this.patientRepository = patientRepository;
        this.vitalRepository = vitalRepository;
    }

    public Vital calculateBMI(List<Vital> vitals) {

        Vital weight = null;
        Vital height = null;

        for (Vital vital : vitals) {
            if (vital.getCode().equals(VitalService.weight.getCode())) {
                weight = vital;
            } else if (vital.getCode().equals(VitalService.height.getCode())) {
                height = vital;
            }
        }

        if (weight != null && height != null) {
            logger.info("Weight and height is available adding BMI into this list");
            float w = Float.valueOf(weight.getValue());
            float h = Float.valueOf(height.getValue());
            return new Vital(weight.getPatientId(), LocalDateTime.now(),
                    VitalService.bmi.getCode(), String.format("%.2f", (w / Math.sqrt(h / 100))), "System Generated");
        } else {
            logger.info("BMI not calculated");
            return null;
        }
    }

    public List<Vital> addVital(List<Vital> vitals) throws CMSException {

        List<Vital> list = new ArrayList<>();

//        vitals = vitals.stream()
//                .filter(vital -> !vital.getCode().equals(VitalService.bmi.getCode()))
//                .collect(Collectors.toList());
//
//        Vital calculatedBMI = calculateBMI(vitals);
//
//        if (calculatedBMI != null) {
//            vitals.add(calculatedBMI);
//        }

        for (Vital vital : vitals) {
            Vital addVital = addVital(vital);
            list.add(addVital);
        }
        return list;
    }

    public Vital addVital(Vital vital) throws CMSException {
        logger.info("adding new vital [" + vital + "]");
        if (!vital.areParametersValid()) {
            logger.error("Invalid parameters [" + vital + "]");
            throw new CMSException(StatusCode.E1005, "Invalid Vital Details");
        }
        boolean patientExists = patientRepository.existsById(vital.getPatientId());
        if (!patientExists) {
            logger.error("Patient ID [" + vital.getPatientId() + "] does not match");
            throw new CMSException(StatusCode.E1005, "Patient ID not valid");
        }
        boolean match = listSupportedVitals().stream()
                .anyMatch(vitalMaster -> vitalMaster.getCode().equals(vital.getCode()));

        if (!match) {
            logger.error("Vital code [" + vital.getCode() + "] does not exist in master");
            throw new CMSException(StatusCode.E1005, "Vital sign code does not match");
        }
        vital.setTakenTime(LocalDateTime.now());
        return vitalRepository.save(vital);
    }

    public void deleteVital(String vitalId) throws CMSException {
        logger.info("Deleting vital [" + vitalId + "]");
        boolean exists = vitalRepository.existsById(vitalId);
        if (!exists) {
            logger.error("Vital ID [" + vitalId + "] does not exists");
            throw new CMSException(StatusCode.E1005, "Invalid Vital ID");

        }

        vitalRepository.deleteById(vitalId);
    }

    public Map<String, List<Vital>> listVitals(String patientId, LocalDateTime start, LocalDateTime end) {
        logger.info("Listing vitals for [" + patientId + "] from[" + start + "] to[" + end + "]");
        return vitalRepository.findPatientVitals(patientId, start, end, Sort.Order.asc("timeTaken"))
                .stream()
                .collect(Collectors.groupingBy(Vital::getCode));
    }

    public List<VitalMaster> listSupportedVitals() {
        return vitalMasters;
    }

    public List<VitalMaster> listOfDefaultVitals() {
        return defaultMasters;
    }

    public Vital modify(String vitalId, Vital vital) throws CMSException {
        logger.info("modifying vital [" + vitalId + "]");

        Vital persistedVital = vitalRepository.findById(vitalId)
                .orElseThrow(() -> {
                    logger.error("Vital ID not valid [" + vitalId + "]");
                    return new CMSException(StatusCode.E1005, "Invalid Vital ID");
                });
        persistedVital.copyVitals(vital);
        return vitalRepository.save(persistedVital);
    }
}
