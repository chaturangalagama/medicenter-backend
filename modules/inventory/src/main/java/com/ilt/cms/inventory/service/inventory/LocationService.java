package com.ilt.cms.inventory.service.inventory;

import com.ilt.cms.core.entity.item.DrugItem;
import com.ilt.cms.inventory.db.service.interfaces.LocationDatabaseService;
import com.ilt.cms.inventory.model.common.UOM;
import com.ilt.cms.inventory.model.inventory.Inventory;
import com.ilt.cms.inventory.model.inventory.Location;
import com.ilt.cms.inventory.service.common.UOMMatrixService;
import com.ilt.cms.inventory.service.common.UOMService;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationService {
    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    private LocationDatabaseService locationDatabaseService;

    private DrugItemService drugItemService;

    private UOMService uomService;

    private UOMMatrixService uomMatrixService;

    public LocationService(LocationDatabaseService locationDatabaseService, DrugItemService drugItemService,
                           UOMService uomService, UOMMatrixService uomMatrixService){
        this.locationDatabaseService = locationDatabaseService;
        this.drugItemService = drugItemService;
        this.uomService = uomService;
        this.uomMatrixService = uomMatrixService;
    }

    public Location findLocationByClinicId(String clinicId) throws CMSException {
        Optional<Location> locationOpt = locationDatabaseService.findLocationByClinicId(clinicId);

        if(!locationOpt.isPresent()){
            logger.info("Create new location for clinic["+clinicId+"]");
            Location location = new Location(clinicId);
            locationOpt = Optional.of(location);
        }
        return locationOpt.get();
    }

    public List<Inventory> findLocationByClinicIdAndItemIds(int page, int size, String clinicId, List<String> itemIds){
        Optional<Location> locationOpt = locationDatabaseService.findLocationByClinicId(clinicId);
        if(!locationOpt.isPresent()){
            new CMSException(StatusCode.E2000, "Location not found with clinic["+clinicId+"]");
        }
        List<Inventory> inventories = locationOpt.get().getInventory().stream()
                .filter(inventory -> itemIds.contains(inventory.getItemRefId()))
                .collect(Collectors.toList());
        return inventories;
    }

    public Location saveLocation(Location location){
        return locationDatabaseService.saveLocation(location);
    }

    public Location modifyLocation(String locationId, Location location) throws CMSException {
        logger.info("ModifyLocation to location["+locationId+"]");
        Optional<Location> locationOpt = locationDatabaseService.findLocationById(locationId);
        if(!locationOpt.isPresent()){
            throw new CMSException(StatusCode.E2000);
        }
        Location curLocation = locationOpt.get();
        curLocation.setInventory(location.getInventory());

        return locationDatabaseService.saveLocation(curLocation);
    }

    public UOM findBaseUomByItemId(String itemId) throws CMSException {
        DrugItem item = drugItemService.findDrugItemById(itemId);

        Optional<UOM> baseUomOpt = uomService.findUomByCode(item.getBaseUom());
        if(!baseUomOpt.isPresent()){
            baseUomOpt = Optional.of(uomService.save(new UOM(item.getBaseUom(), item.getBaseUom())));
        }

        return baseUomOpt.get();
    }

    public double findInventoryQuantityByClinicIdAndItemCodeAndUom(String clinicId, String itemCode, String uom) throws CMSException {
        DrugItem item = drugItemService.findDrugItemByClinicIdAndItemCode(clinicId, itemCode);

        Optional<Location> locationOpt = locationDatabaseService.findLocationByClinicId(clinicId);
        if(!locationOpt.isPresent()){
            return 0;
        }
        Location location = locationOpt.get();
        List<Inventory> inventories = location.getInventory();
        double ratio = uomMatrixService.findRatio(item.getBaseUom(), uom);
        int sum = inventories.stream().filter(inventory -> inventory.getItemRefId().equals(item.getId())
                && (inventory.getExpiryDate() == null || inventory.getExpiryDate().isAfter(LocalDate.now())))
                .mapToInt(Inventory::getAvailableCount).sum();
        return (sum * ratio)/100;
    }
}
