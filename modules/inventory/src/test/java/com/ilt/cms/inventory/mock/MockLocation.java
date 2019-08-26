package com.ilt.cms.inventory.mock;

import com.ilt.cms.inventory.model.common.UOM;
import com.ilt.cms.inventory.model.inventory.Inventory;
import com.ilt.cms.inventory.model.inventory.Location;

import java.time.LocalDate;
import java.util.Arrays;

public class MockLocation {

    public static Location mockLocation(){
        Location location = new Location("2829h3k221923m2");
        location.setId("9k323j9348l32k");
        location.setInventory(Arrays.asList(
                mockInventory("j23hjk32992jkjlaaa", "BN-00001","BOX", LocalDate.now(), 2200, false),
                mockInventory("j23hjk32992jfkjlaaa", "BN-00002","BOX", LocalDate.now(), 2200, true)));
        return location;
    }

    public static Inventory mockInventory(String itemRefId, String batchNumber, String uom, LocalDate expiryDate,
                                          int availableCount, boolean isLowStockLevel){

        Inventory inventory = new Inventory(itemRefId, batchNumber, uom, expiryDate, availableCount, isLowStockLevel);
        return inventory;
    }
}
