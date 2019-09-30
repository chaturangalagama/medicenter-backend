package com.ilt.cms.pm.business.service.clinic;

import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.doctor.Doctor;
import com.ilt.cms.core.entity.patient.PatientNote;
import com.ilt.cms.database.clinic.DoctorDatabaseService;
import com.ilt.cms.database.clinic.PatientNoteDatabaseService;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PatientNoteService {
    private static final Logger logger = LoggerFactory.getLogger(PatientNoteService.class);
    private PatientNoteDatabaseService patientNoteDatabaseService;
    private DoctorDatabaseService doctorDatabaseService;

    public PatientNoteService(PatientNoteDatabaseService patientNoteDatabaseService, DoctorDatabaseService doctorDatabaseService) {
        this.patientNoteDatabaseService = patientNoteDatabaseService;
        this.doctorDatabaseService = doctorDatabaseService;
    }

    private Map<String, String> loadDoctorNames() {
        Map<String, String> doctorNames = new HashMap<>();
        for (Doctor doctor : doctorDatabaseService.findAll()) {
            doctorNames.put(doctor.getId(), doctor.getName());
        }
        return doctorNames;
    }

    private void reverseAndSetDoctorName(List<PatientNote.PatientNoteDetails> list, Map<String, String> doctorNames) {
        if (list.size() > 0) {
            PatientNote.PatientNoteDetails noteDetails = list.remove(0);
            reverseAndSetDoctorName(list, doctorNames);
            noteDetails.setDoctorName(doctorNames.get(noteDetails.getDoctorId()));
            list.add(noteDetails);
        }
    }

    public PatientNote patientNote(String patientId) {
        logger.info("Searching for patient note [" + patientId + "]");
        PatientNote patientNote = patientNoteDatabaseService.findByPatientId(patientId);

        if (patientNote == null) {
            logger.info("Patient info still not available sending empty list");
            patientNote = new PatientNote(patientId);
            return patientNote;
        }

        Map<String, String> doctorNames = loadDoctorNames();
        reverseAndSetDoctorName(patientNote.getNoteDetails(), doctorNames);
        return patientNote;
    }

    public PatientNote addNewNote(String patientId, PatientNote.PatientNoteDetails patientNoteDetails) throws CMSException {
        String patientNoteId = patientNoteDetails.generateAndSetId();
        logger.info("New id set for record [" + patientNoteId + "], saving records");
        boolean isValid = patientNoteDetails.areParametersValid();
        if (!isValid) {
            logger.error("Note details is not valid [" + patientNoteDetails + "]");
            throw new CMSException(StatusCode.E1005, "Invalid parameters");
        }
        PatientNote patientNote = patientNoteDatabaseService.addNewPatientNoteDetails(patientId, patientNoteDetails);
        Map<String, String> doctorNames = loadDoctorNames();
        reverseAndSetDoctorName(patientNote.getNoteDetails(), doctorNames);
        return patientNote;
    }

    public PatientNote changeNoteDetailState(String patientId, String patientNoteDetailId, Status newStatus) {
        logger.info("Changing state of note patientId[" + patientId + "] patientNoteDetailId[" + patientNoteDetailId
                + "] newStatus[" + newStatus + "]");

        PatientNote patientNote = patientNoteDatabaseService.updatePatientNoteDetailsStatus(patientNoteDetailId, newStatus);

        return patientNote;
    }
}
