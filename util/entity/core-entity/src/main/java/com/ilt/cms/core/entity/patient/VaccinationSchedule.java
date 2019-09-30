package com.ilt.cms.core.entity.patient;

import java.util.Date;

public class VaccinationSchedule {

    private String vaccineId;
    private Date scheduledDate;

    public VaccinationSchedule(){

    }

    public VaccinationSchedule(String vaccineId, Date scheduledDate){
        this.vaccineId = vaccineId;
        this.scheduledDate = scheduledDate;
    }
    public String getVaccineId() {
        return vaccineId;
    }

    public void setVaccineId(String vaccineId) {
        this.vaccineId = vaccineId;
    }

    public Date getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(Date scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    @Override
    public String toString() {
        return "VaccinationSchedule{" +
                "vaccineId='" + vaccineId + '\'' +
                ", scheduledDate=" + scheduledDate +
                '}';
    }
}
