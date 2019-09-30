package com.ilt.cms.api.entity.patient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lippo.cms.util.CMSConstant;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PatientVaccination {

    private String id;
    private String doctorId;
    private String vaccineId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CMSConstant.JSON_DATE_FORMAT)
    private Date givenDate;
    private String placeGiven;
    private List<VaccinationSchedule> vaccinationSchedules = new ArrayList<>();


}
