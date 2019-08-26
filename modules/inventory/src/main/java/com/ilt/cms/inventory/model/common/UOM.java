package com.ilt.cms.inventory.model.common;


public class UOM {
    private String code;

    private String displayName;

    public UOM() {
    }

    public UOM(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "UOM{" +
                "code='" + code + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
