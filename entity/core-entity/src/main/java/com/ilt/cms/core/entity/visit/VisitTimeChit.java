package com.ilt.cms.core.entity.visit;

import com.ilt.cms.core.entity.PersistedObject;

import java.time.LocalDateTime;

public class VisitTimeChit extends PersistedObject {

    private String patientVisitId;
    private LocalDateTime from;
    private LocalDateTime to;

    public VisitTimeChit(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    public VisitTimeChit() {
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
    }

    public String getPatientVisitId() {
        return patientVisitId;
    }

    public void setPatientVisitId(String patientVisitId) {
        this.patientVisitId = patientVisitId;
    }

    @Override
    public String toString() {
        return "VisitTimeChit{" +
                "patientVisitId='" + patientVisitId + '\'' +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}
