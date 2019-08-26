package com.ilt.cms.api.entity.patient;

import lombok.*;


@Getter
@Setter
@ToString
public class PatientCompany {

    private String name;
    private String address;
    private String postalCode;
    private String occupation;


    public PatientCompany() {
    }

    public PatientCompany(String name, String address, String postalCode, String occupation) {
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.occupation = occupation;
    }


}
