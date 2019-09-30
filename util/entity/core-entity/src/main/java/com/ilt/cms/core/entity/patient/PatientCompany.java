package com.ilt.cms.core.entity.patient;

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

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getOccupation() {
        return occupation;
    }

    @Override
    public String toString() {
        return "PatientCompany{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", occupation='" + occupation + '\'' +
                '}';
    }
}
