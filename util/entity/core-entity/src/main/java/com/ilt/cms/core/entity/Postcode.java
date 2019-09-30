package com.ilt.cms.core.entity;

import org.springframework.data.mongodb.core.index.Indexed;

public class Postcode extends PersistedObject {

    @Indexed(unique = true)
    private String code;
    private String address;

    public Postcode() {
    }

    public Postcode(String code, String address) {
        this.code = code;
        this.address = address;
    }

    public String getCode() {
        return code;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Postcode{" +
                "code='" + code + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
