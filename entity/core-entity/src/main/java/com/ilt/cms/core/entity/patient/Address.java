package com.ilt.cms.core.entity.patient;

import static com.lippo.commons.util.CommonUtils.isStringValid;

public class Address {
    private String address;
    private String country;
    private String postalCode;

    public Address(String address, String country, String postalCode) {
        this.address = address;
        this.country = country;
        this.postalCode = postalCode;
    }


    public Address() {
    }

    public String getAddress() {
        return address;
    }

    public String getCountry() {
        return country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public boolean areParametersValid() {
        return isStringValid(address) && isStringValid(country);
    }
}
