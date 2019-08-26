package com.ilt.cms.core.entity.patient;

import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.Status;
import com.lippo.commons.util.CommonUtils;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PatientNote extends PersistedObject {

    @Indexed(unique = true)
    private String patientId;
    private List<PatientNoteDetails> noteDetails = new ArrayList<>();

    public PatientNote(String patientId) {
        this.patientId = patientId;
    }

    public enum TypeOfProblem {
        LONG_TERM, SHORT_TERM
    }

    public static class PatientNoteDetails {
        private String patientNoteId;
        private String note;
        private String doctorId;
        private transient String doctorName;
        private LocalDateTime addedDateTime;
        private Status status;
        private TypeOfProblem typeOfProblem;
        private LocalDateTime dateOfOnSet;

        public PatientNoteDetails() {
        }

        public PatientNoteDetails(String note, String doctorId, LocalDateTime addedDateTime, Status status,
                                  TypeOfProblem typeOfProblem, LocalDateTime dateOfOnSet) {
            this.note = note;
            this.doctorId = doctorId;
            this.addedDateTime = addedDateTime;
            this.status = status;
            this.typeOfProblem = typeOfProblem;
            this.dateOfOnSet = dateOfOnSet;
        }

        public boolean areParametersValid() {
            return CommonUtils.isStringValid(note, doctorId) && addedDateTime != null && status != null;
        }

        public String generateAndSetId() {
            patientNoteId = CommonUtils.idGenerator();
            return patientNoteId;
        }

        public String getPatientNoteId() {
            return patientNoteId;
        }

        public void setPatientNoteId(String patientNoteId) {
            this.patientNoteId = patientNoteId;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(String doctorId) {
            this.doctorId = doctorId;
        }

        public String getDoctorName() {
            return doctorName;
        }

        public void setDoctorName(String doctorName) {
            this.doctorName = doctorName;
        }

        public LocalDateTime getAddedDateTime() {
            return addedDateTime;
        }

        public void setAddedDateTime(LocalDateTime addedDateTime) {
            this.addedDateTime = addedDateTime;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public TypeOfProblem getTypeOfProblem() {
            return typeOfProblem;
        }

        public void setTypeOfProblem(TypeOfProblem typeOfProblem) {
            this.typeOfProblem = typeOfProblem;
        }

        public LocalDateTime getDateOfOnSet() {
            return dateOfOnSet;
        }

        public void setDateOfOnSet(LocalDateTime dateOfOnSet) {
            this.dateOfOnSet = dateOfOnSet;
        }
    }

    public String getPatientId() {
        return patientId;
    }

    public List<PatientNoteDetails> getNoteDetails() {
        return noteDetails;
    }

    public void setNoteDetails(List<PatientNoteDetails> patientNoteDetails){
        this.noteDetails = patientNoteDetails;
    }
}
