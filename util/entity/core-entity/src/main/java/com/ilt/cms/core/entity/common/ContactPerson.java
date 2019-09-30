package com.ilt.cms.core.entity.common;

import com.lippo.commons.util.CommonUtils;

public class ContactPerson {

    private String name;
    private String title;
    private String directNumber;
    private String mobileNumber;
    private String faxNumber;
    private String email;

    public ContactPerson() {
    }

    public ContactPerson(String name, String title, String directNumber, String mobileNumber, String faxNumber, String email) {
        this.name = name;
        this.title = title;
        this.directNumber = directNumber;
        this.mobileNumber = mobileNumber;
        this.faxNumber = faxNumber;
        this.email = email;
    }

    public boolean areParametersValid() {
        return CommonUtils.isStringValid(name, title, directNumber);
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getDirectNumber() {
        return directNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "ContactPerson{" +
                "name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", directNumber='" + directNumber + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", faxNumber='" + faxNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
