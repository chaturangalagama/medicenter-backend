package com.ilt.cms.core.entity.patient;

import java.util.Date;

public class PatientAllergy {

    public enum AllergyType {
        SPECIFIC_DRUG, NAME_STARTING_WITH, NAME_CONTAINING, ALLERGY_GROUP, FOOD, OTHER
    }

    private AllergyType allergyType;
    private String name;
    private String remarks;
    private Date addedDate;

    public PatientAllergy() {
    }

    public PatientAllergy(AllergyType allergyType, String name, String remarks, Date addedDate) {
        this.allergyType = allergyType;
        this.name = name;
        this.remarks = remarks;
        this.addedDate = addedDate;
    }

    public AllergyType getAllergyType() {
        return allergyType;
    }

    public String getName() {
        return name;
    }

    public String getRemarks() {
        return remarks;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    @Override
    public String toString() {
        return "PatientAllergy{" +
                "allergyType=" + allergyType +
                ", name='" + name + '\'' +
                ", remarks='" + remarks + '\'' +
                ", addedDate=" + addedDate +
                '}';
    }
}
