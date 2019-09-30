package com.ilt.cms.core.entity.common;

import static com.lippo.commons.util.CommonUtils.isStringValid;

public class CorporateAddress {

    private String attentionTo;
    private String address;
    private String postalCode;


    public CorporateAddress(String attentionTo, String address, String postalCode) {
        this.attentionTo = attentionTo;
        this.address = address;
        this.postalCode = postalCode;
    }

    public boolean areParametersValid() {
        return isStringValid(address, postalCode);
    }


    public CorporateAddress() {
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getAttentionTo() {
        return attentionTo;
    }

    @Override
    public String toString() {
        return "CorporateAddress{" +
                "attentionTo='" + attentionTo + '\'' +
                ", address='" + address + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}
