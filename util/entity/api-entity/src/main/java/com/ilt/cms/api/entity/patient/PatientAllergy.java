package com.ilt.cms.api.entity.patient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lippo.cms.util.CMSConstant;
import lombok.*;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PatientAllergy {

    public enum AllergyType {
        SPECIFIC_DRUG, NAME_STARTING_WITH, NAME_CONTAINING, ALLERGY_GROUP, FOOD, OTHER
    }

    private AllergyType allergyType;
    private String name;
    private String remarks;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CMSConstant.JSON_DATE_FORMAT)
    private Date addedDate;


}
