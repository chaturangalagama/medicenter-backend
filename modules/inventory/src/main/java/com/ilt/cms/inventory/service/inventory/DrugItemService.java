package com.ilt.cms.inventory.service.inventory;

import com.ilt.cms.core.entity.item.DrugItem;
import com.ilt.cms.core.entity.item.Item;
import com.ilt.cms.database.item.ItemDatabaseService;
import com.ilt.cms.inventory.model.inventory.enums.ItemGroupType;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.StatusCode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DrugItemService {

    private ItemDatabaseService itemDatabaseService;

    public DrugItemService(ItemDatabaseService itemDatabaseService){
        this.itemDatabaseService = itemDatabaseService;
    }

    public DrugItem findDrugItemById(String itemRefId) throws CMSException {
        Optional<Item> itemOpt = itemDatabaseService.findItemByItemId(itemRefId);
        if(!itemOpt.isPresent()){
            throw new CMSException(StatusCode.E2000, "Item not found");
        }
        Item item = itemOpt.get();
        if(!(item instanceof DrugItem)){
            throw new CMSException(StatusCode.E2000, "It is not a drug item");
        }

        return (DrugItem) item;
    }

    public List<DrugItem> findDrugItem(String itemNameRegex, String itemCodeRegex, List<String> supplierIds){
        List<Item> items = itemDatabaseService.findItem(itemNameRegex, itemCodeRegex, supplierIds);
        List<DrugItem> drugItems = items.stream()
                .filter(item -> item instanceof DrugItem)
                .map(item -> (DrugItem)item)
                .collect(Collectors.toList());

        return drugItems;
    }

    public DrugItem findDrugItemByClinicIdAndItemCode(String clinicId, String itemCode) throws CMSException {
        Optional<Item> itemOpt = itemDatabaseService.findByClinicIdAndCode(clinicId, itemCode);
        if(!itemOpt.isPresent()){
            throw new CMSException(StatusCode.E2000, "Item not found");
        }
        Item item = itemOpt.get();
        if(!(item instanceof DrugItem)){
            throw new CMSException(StatusCode.E2000, "Item not a drug type");
        }
        return (DrugItem) item;
    }

    public List<DrugItem> listDrugItem(){
        List<Item> items = itemDatabaseService.findAll();
        return items.stream()
                .filter(item -> item instanceof DrugItem)
                .map(item -> (DrugItem)item)
                .collect(Collectors.toList());
    }

    public DrugItem createDrugItem(DrugItem drugItem){
        return (DrugItem)itemDatabaseService.saveItem(drugItem);
    }

    public List<DrugItem> listDrugItemByClinicId(String clinicId) {
        List<Item> items = itemDatabaseService.findItemByClinicId(clinicId);
        List<DrugItem> drugItems = items.stream()
                .filter(item -> item instanceof DrugItem)
                .map(item -> (DrugItem)item)
                .collect(Collectors.toList());

        return drugItems;
    }

}
