package com.ilt.cms.inventory.mock;

import com.ilt.cms.core.entity.item.*;

import java.util.ArrayList;
import java.util.List;

public class MockDrugItem {



    public static DrugItem mockDrugItem(List<String> clinicIds) {

        DrugItem drugItem = new DrugItem();
        drugItem.setId("j23hjk32992jkjlaaa");
        drugItem.setName("DRUG-Medicine");
        drugItem.setCode("DRUG-VS0001");
        drugItem.setItemType(Item.ItemType.DRUG);
        drugItem.setDescription("A strong new drugs in the world");
        drugItem.setBaseUom("TABLET");
        drugItem.setPurchaseUom("BOX");
        drugItem.setCategory("DRUG");
        drugItem.setCost(new Cost(140, false));
        drugItem.setSellingPrice(new SellingPrice(200, false));
        drugItem.setReorderPoint(10);
        drugItem.setReorderQty(20);
        drugItem.setMinimumOrderQty(1);
        drugItem.setMaximumOrderQty(200);
        drugItem.setSafetyStockQty(100);
        drugItem.setAllowNegativeInventory(true);
        drugItem.setSubItems(null);
        drugItem.setSupplierId("92923902");
        drugItem.setItemFilter(new ItemFilter(new ArrayList<>(clinicIds), new ArrayList<>()));

        drugItem.setDosageUom("TABLET");
        drugItem.setBrandName("brand name");
        drugItem.setBrandNameJapanese("");
        drugItem.setBrandNameChinese("");
        drugItem.setGenericName("generic name");
        drugItem.setGenericNameJapanese("");
        drugItem.setGenericNameChinese("");
        drugItem.setPrintDrugLabel(false);
        drugItem.setRemarks("drug is poison");
        drugItem.setMimsClassification("class");
        drugItem.setDrugType("DRUG");
        drugItem.setDrugBrandedType("brug branded type");
        drugItem.setModeOfAdministration("administration");
        drugItem.setDosageQty(20);
        drugItem.setFrequency("3 tims per day");
        drugItem.setFrequencyCode("3TPY");
        drugItem.setPrecautionaryInstructions("no instructions");
        drugItem.setIndications("indications");
        drugItem.setControlledDrug(true);
        drugItem.setDispensingMultiples(2);
        drugItem.setDosageInstructionCode("TAB");
        return drugItem;
    }
}
