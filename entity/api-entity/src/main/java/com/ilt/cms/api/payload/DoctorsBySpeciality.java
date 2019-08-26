package com.ilt.cms.api.payload;

import com.ilt.cms.api.entity.doctor.SpecialityEntity;
import lombok.*;

import java.util.List;
import java.util.Map;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DoctorsBySpeciality {
    private SpecialityEntity.Practice practice;
    private List<Map<String, Object>> clinics;

    public SpecialityEntity.Practice getPractice() {
        return practice;
    }

    public List<Map<String, Object>> getClinics() {
        return clinics;
    }
}
