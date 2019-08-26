package com.ilt.cms.core.entity.doctor;

import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.consultation.ConsultationTemplate;
import com.lippo.commons.util.CommonUtils;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.ArrayList;
import java.util.List;

public class Doctor extends PersistedObject {

    public enum DoctorGroup {
        LOCUM, FLOATING, ANCHOR
    }

    private String name;
    private String education;
    private String mcr;
    private String nric;
    @Indexed(unique = true, sparse = true)
    private String username;
    private String displayName;
    private DoctorGroup doctorGroup;
    private Speciality.Practice speciality;
    private Status status;
    private List<ConsultationTemplate> consultationTemplates = new ArrayList<>();
    private String _migrationSyncId;

    public Doctor() {
    }

    public Doctor(String name, String education, Speciality.Practice speciality, DoctorGroup doctorGroup, Status status,
                  List<ConsultationTemplate> consultationTemplates) {
        this.name = name;
        this.education = education;
        this.speciality = speciality;
        this.doctorGroup = doctorGroup;
        this.status = status;
        this.consultationTemplates = consultationTemplates;
    }

    public boolean areParameterValid() {
        return CommonUtils.isStringValid(name, education) && speciality != null && doctorGroup != null
                && consultationTemplates.stream().allMatch(ConsultationTemplate::areParametersValid);
    }

    public void copy(Doctor doctor) {
        name = doctor.name;
        education = doctor.education;
        speciality = doctor.speciality;
        doctorGroup = doctor.doctorGroup;
        status = doctor.status;
        consultationTemplates = doctor.consultationTemplates;
        mcr = doctor.mcr;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public Speciality.Practice getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality.Practice speciality) {
        this.speciality = speciality;
    }


    public DoctorGroup getDoctorGroup() {
        return doctorGroup;
    }

    public void setDoctorGroup(DoctorGroup doctorGroup) {
        this.doctorGroup = doctorGroup;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<ConsultationTemplate> getConsultationTemplates() {
        return consultationTemplates;
    }

    public void setConsultationTemplates(List<ConsultationTemplate> consultationTemplates) {
        this.consultationTemplates = consultationTemplates;
    }

    public String getMcr() {
        return mcr;
    }

    public void setMcr(String mcr) {
        this.mcr = mcr;
    }

    public String get_migrationSyncId() {
        return _migrationSyncId;
    }

    public void set_migrationSyncId(String _migrationSyncId) {
        this._migrationSyncId = _migrationSyncId;
    }

    public String getNric() {
        return nric;
    }

    public void setNric(String nric) {
        this.nric = nric;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "name='" + name + '\'' +
                ", education='" + education + '\'' +
                ", mcr='" + mcr + '\'' +
                ", username='" + username + '\'' +
                ", doctorGroup=" + doctorGroup +
                ", speciality=" + speciality +
                ", status=" + status +
                ", consultationTemplates=" + consultationTemplates +
                '}';
    }


}
