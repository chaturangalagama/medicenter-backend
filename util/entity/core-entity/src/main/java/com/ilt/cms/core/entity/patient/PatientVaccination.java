package com.ilt.cms.core.entity.patient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.lippo.commons.util.CommonUtils.isStringValid;

public class PatientVaccination {

    private String id;
    private String doctorId;
    private String vaccineId;
    private Date givenDate;
    private String placeGiven;
    private List<VaccinationSchedule> vaccinationSchedules = new ArrayList<>();


    public boolean areParametersValid() {
        return isStringValid(vaccineId, placeGiven) && givenDate != null
                && vaccinationSchedules.stream()
                .allMatch(vs -> isStringValid(vs.getVaccineId()) && vs.getScheduledDate() != null);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVaccineId() {
        return vaccineId;
    }

    public void setVaccineId(String vaccineId) {
        this.vaccineId = vaccineId;
    }

    public Date getGivenDate() {
        return givenDate;
    }

    public void setGivenDate(Date givenDate) {
        this.givenDate = givenDate;
    }

    public String getPlaceGiven() {
        return placeGiven;
    }

    public void setPlaceGiven(String placeGiven) {
        this.placeGiven = placeGiven;
    }

    public List<VaccinationSchedule> getVaccinationSchedules() {
        return vaccinationSchedules;
    }

    public void setVaccinationSchedules(List<VaccinationSchedule> vaccinationSchedules) {
        this.vaccinationSchedules = vaccinationSchedules;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorId(){
        return doctorId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientVaccination that = (PatientVaccination) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PatientVaccination{" +
                "id='" + id + '\'' +
                ", doctorId='" + doctorId + '\'' +
                ", vaccineId='" + vaccineId + '\'' +
                ", givenDate=" + givenDate +
                ", placeGiven='" + placeGiven + '\'' +
                ", vaccinationSchedules=" + vaccinationSchedules +
                '}';
    }
}
