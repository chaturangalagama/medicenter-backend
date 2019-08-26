package com.ilt.cms.core.entity.patient;

import com.lippo.commons.util.CommonUtils;

public class ContactNumber {

    private String number;

    public ContactNumber() {
    }

    public ContactNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public String formattedNumber() {
        if (number != null) {
            return number
                    .replace(" ", "")
                    .replace("+", "");
        } else {
            return null;
        }
    }

    public ContactNumber update(String number) {
        this.number = number;
        return this;
    }

    @Override
    public String toString() {
        return "ContactNumber{" +
                ", number='" + number + '\'' +
                '}';
    }

    public boolean areParametersValid() {
        return CommonUtils.isStringValid(number);
    }
}
