package com.ilt.cms.core.entity.vital;

import java.util.Objects;

public class VitalMaster {

    private String code;
    private String name;
    private String uom;

    public VitalMaster() {
    }

    public VitalMaster(String code, String name, String uom) {
        this.code = code;
        this.name = name;
        this.uom = uom;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getUom() {
        return uom;
    }

    @Override
    public String toString() {
        return "VitalMaster{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", uom='" + uom + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VitalMaster that = (VitalMaster) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {

        return Objects.hash(code);
    }
}
