package com.ilt.cms.database.clinic;

import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.patient.PatientNote;

public interface PatientNoteDatabaseService {
    PatientNote findByPatientId(String patientId);

    PatientNote addNewPatientNoteDetails(String patientId, PatientNote.PatientNoteDetails patientNoteDetails);

    PatientNote updatePatientNoteDetailsStatus(String patientNoteDetailId, Status newStatus);
}
