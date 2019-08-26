package com.ilt.cms.inventory.mock;

import com.ilt.cms.core.entity.Clinic;

public class MockClinic {

    public static Clinic mockClinic(String id, String name){

        Clinic clinic = new Clinic();
        clinic.setId(id);
        clinic.setName(name);
        return clinic;
    }
}
