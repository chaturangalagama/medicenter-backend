package com.ilt.cms.inventory.model.inventory;

import com.ilt.cms.core.entity.PersistedObject;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.ArrayList;
import java.util.List;

@CompoundIndexes({
        @CompoundIndex(name = "index_unique", unique = true, def = "{'clinicId' : 1, 'inventory.itemRefId' : 1, 'inventory.inventoryId' : 1 }"),
})
public class Location extends PersistedObject {


    private String clinicId;
    private List<Inventory> inventory;

    public Location() {
        this.inventory = new ArrayList<>();
    }

    public Location(String clinicId) {
        this.clinicId = clinicId;
        this.inventory = new ArrayList<>();
    }

    public String getClinicId() {
        return clinicId;
    }

    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }

    public List<Inventory> getInventory() {
        return inventory;
    }

    public void setInventory(List<Inventory> inventory) {
        this.inventory = inventory;
    }

    @Override
    public String toString() {
        return "Location{" +
                "clinicId='" + clinicId + '\'' +
                ", inventory=" + inventory +
                ", id='" + id + '\'' +
                '}';
    }
}
