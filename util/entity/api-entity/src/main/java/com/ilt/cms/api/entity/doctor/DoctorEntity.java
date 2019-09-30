package com.ilt.cms.api.entity.doctor;

import com.ilt.cms.api.entity.common.Status;
import com.ilt.cms.api.entity.consultation.ConsultationTemplate;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DoctorEntity {
    public enum DoctorGroup {
        LOCUM, FLOATING, ANCHOR
    }
    private String id;
    private String name;
    private String education;
    private String mcr;
    private String username;
    private String displayName;
    private DoctorGroup doctorGroup;
    private SpecialityEntity.Practice speciality;
    private Status status;
    private List<ConsultationTemplate> consultationTemplates = new ArrayList<>();
}
