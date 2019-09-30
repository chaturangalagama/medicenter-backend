package com.ilt.cms.pm.integration.mapper.clinic;

import com.ilt.cms.api.entity.doctor.SpecialityEntity;
import com.ilt.cms.core.entity.consultation.ConsultationTemplate;
import com.ilt.cms.api.entity.doctor.DoctorEntity;
import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.doctor.Doctor;
import com.ilt.cms.core.entity.doctor.Speciality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class DoctorMapper {
    private static Logger logger = LoggerFactory.getLogger(DoctorMapper.class);
    public static Doctor mapToCore(DoctorEntity doctorEntity){
        if(doctorEntity == null){
            return null;
        }
        Doctor doctor = new Doctor();
        doctor.setId(doctorEntity.getId());
        doctor.setUsername(doctorEntity.getUsername());
        doctor.setName(doctorEntity.getName());
        doctor.setEducation(doctorEntity.getEducation());
        doctor.setMcr(doctorEntity.getMcr());
        doctor.setDisplayName(doctorEntity.getDisplayName());
        if(doctorEntity.getStatus() != null) {
            doctor.setStatus(Status.valueOf(doctorEntity.getStatus().name()));
        }
        if(doctorEntity.getSpeciality() != null) {
            doctor.setSpeciality(Speciality.Practice.valueOf(doctorEntity.getSpeciality().name()));
        }
        if(doctorEntity.getDoctorGroup() != null) {
            doctor.setDoctorGroup(Doctor.DoctorGroup.valueOf(doctorEntity.getDoctorGroup().name()));
        }
        List<ConsultationTemplate> consultationTemplates =
        doctorEntity.getConsultationTemplates().stream().map(DoctorMapper::mapToConsultationTemplateCore).collect(Collectors.toList());
        doctor.setConsultationTemplates(consultationTemplates);

        return doctor;
    }

    public static DoctorEntity mapToEntity(Doctor doctor) {
        if(doctor == null){
            return null;
        }
        DoctorEntity doctorEntity = new DoctorEntity();
        doctorEntity.setId(doctor.getId());
        doctorEntity.setUsername(doctor.getUsername());
        doctorEntity.setName(doctor.getName());
        doctorEntity.setEducation(doctor.getEducation());
        doctorEntity.setMcr(doctor.getMcr());
        doctorEntity.setDisplayName(doctor.getDisplayName());
        if(doctor.getStatus() != null) {
            doctorEntity.setStatus(com.ilt.cms.api.entity.common.Status.valueOf(doctor.getStatus().name()));
        }
        if(doctor.getSpeciality() != null) {
            doctorEntity.setSpeciality(SpecialityEntity.Practice.valueOf(doctor.getSpeciality().name()));
        }
        if(doctor.getDoctorGroup() != null) {
            doctorEntity.setDoctorGroup(DoctorEntity.DoctorGroup.valueOf(doctor.getDoctorGroup().name()));
        }
        List<com.ilt.cms.api.entity.consultation.ConsultationTemplate> consultationTemplates =
        doctor.getConsultationTemplates().stream().map(DoctorMapper::mapToConsultationTemplateEntity).collect(Collectors.toList());
        doctorEntity.setConsultationTemplates(consultationTemplates);
        return doctorEntity;
    }

    public static ConsultationTemplate mapToConsultationTemplateCore(com.ilt.cms.api.entity.consultation.ConsultationTemplate consultationTemplateEntity){
        if(consultationTemplateEntity == null){
            return null;
        }
        ConsultationTemplate consultationTemplate = new ConsultationTemplate();
        consultationTemplate.setName(consultationTemplateEntity.getName());
        consultationTemplate.setTemplate(consultationTemplateEntity.getTemplate());
        consultationTemplate.setType(consultationTemplateEntity.getType());
        return consultationTemplate;
    }

    public static com.ilt.cms.api.entity.consultation.ConsultationTemplate mapToConsultationTemplateEntity(ConsultationTemplate consultationTemplate){
        if(consultationTemplate == null){
            return null;
        }
        com.ilt.cms.api.entity.consultation.ConsultationTemplate consultationTemplateEntity = new com.ilt.cms.api.entity.consultation.ConsultationTemplate();
        consultationTemplateEntity.setName(consultationTemplate.getName());
        consultationTemplateEntity.setTemplate(consultationTemplate.getTemplate());
        consultationTemplateEntity.setType(consultationTemplate.getType());
        return consultationTemplateEntity;
    }

}