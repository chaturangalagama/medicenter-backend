package com.ilt.cms.api.entity.patient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ilt.cms.api.entity.common.Status;
import com.lippo.cms.util.CMSConstant;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PatientNoteEntity {

    public enum TypeOfProblem {
        LONG_TERM, SHORT_TERM
    }

    private String id;
    private String patientId;
    private List<PatientNoteDetails> noteDetails = new ArrayList<>();

    public PatientNoteEntity(String patientId) {
        this.patientId = patientId;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class PatientNoteDetails {
        private String patientNoteId;
        private String note;
        private String doctorId;
        private transient String doctorName;
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
        @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
        private LocalDateTime addedDateTime;
        private Status status;
        private PatientNoteEntity.TypeOfProblem typeOfProblem;
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
        @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
        private LocalDateTime dateOfOnSet;

        public PatientNoteDetails(String note, String doctorId, LocalDateTime addedDateTime, Status status) {
            this.note = note;
            this.doctorId = doctorId;
            this.addedDateTime = addedDateTime;
            this.status = status;
        }


    }

}
