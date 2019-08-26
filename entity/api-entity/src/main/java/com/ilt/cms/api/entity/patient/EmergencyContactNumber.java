package com.ilt.cms.api.entity.patient;

import com.ilt.cms.api.entity.common.ContactNumber;
import com.ilt.cms.api.entity.common.Relationship;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class EmergencyContactNumber extends ContactNumber {

    private String name;
    private Relationship relationship;

    public EmergencyContactNumber(String name, Relationship relationship, String number){
        super(number);
        this.name = name;
        this.relationship = relationship;
    }

}
