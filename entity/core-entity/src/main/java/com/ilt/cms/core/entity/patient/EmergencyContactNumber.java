package com.ilt.cms.core.entity.patient;

import com.ilt.cms.core.entity.common.Relationship;

public class EmergencyContactNumber extends ContactNumber {

    private String name;
    private Relationship relationship;

    public EmergencyContactNumber() {
    }

    public EmergencyContactNumber(String name, Relationship relationship, String number) {
        super(number);
        this.name = name;
        this.relationship = relationship;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Relationship getRelationship() {
        return relationship;
    }

    public void setRelationship(Relationship relationship) {
        this.relationship = relationship;
    }

    @Override
    public String toString() {
        return "EmergencyContactNumber{" +
                "name='" + name + '\'' +
                ", relationship=" + relationship +
                '}';
    }
}
