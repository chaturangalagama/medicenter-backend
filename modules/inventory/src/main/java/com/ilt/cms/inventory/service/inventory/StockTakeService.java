package com.ilt.cms.inventory.service.inventory;

import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.core.entity.item.DrugItem;
import com.ilt.cms.core.entity.item.Item;
import com.ilt.cms.core.entity.supplier.Supplier;
import com.ilt.cms.database.SupplierDatabaseService;
import com.ilt.cms.database.clinic.ClinicDatabaseService;
import com.ilt.cms.database.item.ItemDatabaseService;
import com.ilt.cms.inventory.db.service.interfaces.LocationDatabaseService;
import com.ilt.cms.inventory.db.service.interfaces.StockTakeDatabaseService;
import com.ilt.cms.inventory.model.inventory.Inventory;
import com.ilt.cms.inventory.model.inventory.Location;
import com.ilt.cms.inventory.model.inventory.StockCountItem;
import com.ilt.cms.inventory.model.inventory.StockTake;
import com.ilt.cms.inventory.model.inventory.enums.*;
import com.ilt.cms.inventory.service.common.UOMMatrixService;
import com.lippo.cms.exception.CMSException;
import com.lippo.cms.util.CMSConstant;
import com.lippo.commons.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.ilt.cms.inventory.model.inventory.enums.StockTakeStatus.IN_PROCESS_FIRST_STOCK_TAKE;

@Service
public class StockTakeService {
    private static final Logger logger = LoggerFactory.getLogger(StockTakeService.class);

    private ItemDatabaseService itemDatabaseService;

    private LocationDatabaseService locationDatabaseService;

    private StockTakeDatabaseService stockTakeDatabaseService;

    private UOMMatrixService uomMatrixService;

    private SupplierDatabaseService supplierDatabaseService;

    private ClinicDatabaseService clinicDatabaseService;

    public StockTakeService(ItemDatabaseService itemDatabaseService, LocationDatabaseService locationDatabaseService,
                            StockTakeDatabaseService stockTakeDatabaseService, UOMMatrixService uomMatrixService,
                            SupplierDatabaseService supplierDatabaseService, ClinicDatabaseService clinicDatabaseService) {
        this.itemDatabaseService = itemDatabaseService;
        this.locationDatabaseService = locationDatabaseService;
        this.stockTakeDatabaseService = stockTakeDatabaseService;
        this.uomMatrixService = uomMatrixService;
        this.supplierDatabaseService = supplierDatabaseService;
        this.clinicDatabaseService = clinicDatabaseService;
    }

    public Map<String, Object> searchInventory(int page, int size, String clinicId,
                                               ItemGroupType itemGroup, String stockAlert,
                                               String itemNameRegex, String supplierNameRegex,
                                               String itemCodeRegex) {
        logger.info("search inventory by page["+page+"], size["+size+"], clinicId["+clinicId+"], itemGroup["+itemGroup+"]," +
                " stockAlert["+stockAlert+"], itemNameRegex["+itemNameRegex+"], " +
                "supplierNameRegex["+supplierNameRegex+"], itemCodeRegex["+itemCodeRegex+"]");
        //init
        if(itemNameRegex == null){
            itemNameRegex = "";
        }
        if(supplierNameRegex == null){
            supplierNameRegex = "";
        }
        if (itemCodeRegex == null) {
            itemCodeRegex = "";
        }
        List<Supplier> suppliers = supplierDatabaseService.findAllByNameRegex(supplierNameRegex);
        logger.info("suppliers size ["+suppliers.size()+"]");

        List<String> supplierIds = suppliers.stream()
                .map(Supplier::getId)
                .collect(Collectors.toList());

        Optional<Location> locationOpt = locationDatabaseService.findLocationByClinicId(clinicId);

        Location location = locationOpt.orElse(new Location(clinicId));

        List<Inventory> inventories = location.getInventory();

        boolean isLowLevel = false;
        boolean isExpired = false;
        if(StockAlertType.INVENTORY_LOW.name().equals(stockAlert)){
            isLowLevel = true;
        }
        if(StockAlertType.EXPIRY.name().equals(stockAlert)){
            isExpired = true;
        }

        Map<String, Object> inventoriesMap = new HashMap<>();
        List<Item> items;
        if (supplierIds == null || supplierIds.size() == 0) {
            items = itemDatabaseService.findItem(clinicId, itemNameRegex, itemCodeRegex);
            logger.info("Found item size ["+items.size()+"]");
        } else{
            items = itemDatabaseService.findItem(clinicId, itemNameRegex, itemCodeRegex, supplierIds);
            logger.info("Found item size ["+items.size()+"]");
        }
        List<DrugItem> drugItems = items.stream()
                .filter(item -> item instanceof DrugItem)
                .map(item-> (DrugItem)item).collect(Collectors.toList());
        inventories = prepareDrugInventory(inventories, drugItems, isLowLevel, isExpired);
        int listSize = inventories.size();
        int startIndex;
        int endIndex;
        int lastPageSize = listSize % size;
        int totalPage = listSize / size + (lastPageSize == 0 ? 0 : 1);
        startIndex = page * size;
        endIndex = (startIndex + size) >= listSize ? startIndex + lastPageSize : startIndex + size;
        inventories = inventories.subList(startIndex, endIndex);
        inventoriesMap.put(CMSConstant.PAYLOAD_KEY_CONTENT, inventories);
        inventoriesMap.put(CMSConstant.PAYLOAD_KEY_NUMBER, page);
        inventoriesMap.put(CMSConstant.PAYLOAD_KEY_TOTAL_PAGES, totalPage);
        inventoriesMap.put(CMSConstant.PAYLOAD_KEY_TOTAL_ELEMENTS, listSize);
        return inventoriesMap;

    }

    public List<StockTake> listAllStockTake(String clinicId, int size, String sortField, Sort.Direction direction){
        return stockTakeDatabaseService.findStockTakeByClinicId(clinicId, size, sortField, direction);
    }

    public StockTake createStockTake(String clinicId, LocalDate startDate, LocalTime startTime, String countName,
                                     StockCountType stockCountType, ItemRange itemRange) throws CMSException {
        Optional<Location> locationOpt = locationDatabaseService.findLocationByClinicId(clinicId);
        if(!locationOpt.isPresent()){
            throw new CMSException(StatusCode.E2000, "inventory not found");
        }
        StockTake stockTake = new StockTake(generateStockTakeName(clinicId, startDate, startTime), startDate, startTime, clinicId,
                stockCountType, itemRange != null ? itemRange.name() : null, countName, IN_PROCESS_FIRST_STOCK_TAKE,
                StockTakeApproveStatus.NOT_APPROVED);
        List<Item> items = new ArrayList<>();
        if(stockCountType == StockCountType.PARTIAL) {
            String itemNameRegex = "";
            switch (itemRange) {
                case A_E:
                    itemNameRegex = "^[A-E]";
                    break;
                case F_J:
                    itemNameRegex = "^[F-J]";
                    break;
                case K_O:
                    itemNameRegex = "^[K-O]";
                    break;
                case P_T:
                    itemNameRegex = "^[P-T]";
                    break;
                case U_Z:
                    itemNameRegex = "^[U-Z]";
                    break;
            }

            items = itemDatabaseService.findItem(clinicId, itemNameRegex, "");
        }else if(stockCountType == StockCountType.FULL){
            items = itemDatabaseService.findItemByClinicId(clinicId);
        }
        List<String> itemRefIds = items.stream()
                .map(Item::getId)
                .collect(Collectors.toList());
        Location location = locationOpt.orElse(new Location(clinicId));
        List<Inventory> inventories = location.getInventory().stream()
                .filter(inventory -> itemRefIds.contains(inventory.getItemRefId()))
                .collect(Collectors.toList());
        List<StockCountItem> stockCountItems = inventories.stream()
                .map(inventory -> {
                    try {
                        return conventInventoryToStockCountItem(inventory);
                    } catch (CMSException e) {
                        logger.error(e.getMessage(), e);
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        stockTake.setStockCountItems(stockCountItems);
        return stockTakeDatabaseService.saveStockTake(stockTake);

    }

    /*public Map<String, Object> searchStockCountItems(String stockTakeId, ItemGroupType itemGroupType, Boolean isCount,
                                                      String searchName, int page, int size) throws CMSException {
        Optional<StockTake> stockTakeOpt = stockTakeDatabaseService.findStockTakeById(stockTakeId);
        if(!stockTakeOpt.isPresent()){
            throw new CMSException(StatusCode.E2000, "Stock take not found");
        }
        StockTake stockTake = stockTakeOpt.get();
        StockTakeStatus stockTakeStatus = stockTake.getStockTakeStatus();
        List<StockCountItem> stockCountItems = stockTake.getStockCountItems();
        if(itemGroupType != null){
            stockCountItems = stockCountItems.stream()
                    .filter(stockCountItem -> stockCountItem.getItemType().equals(itemGroupType.name()))
                    .collect(Collectors.toList());
        }
        if(isCount != null){
            if(stockTakeStatus == StockTakeStatus.IN_PROCESS_FIRST_STOCK_TAKE) {
                if (isCount) {
                    stockCountItems = stockCountItems.stream()
                            .filter(stockCountItem -> stockCountItem.getFirstQuantity() != null)
                            .collect(Collectors.toList());
                } else {
                    stockCountItems = stockCountItems.stream()
                            .filter(stockCountItem -> stockCountItem.getFirstQuantity() == null)
                            .collect(Collectors.toList());
                }
            }
            if(stockTakeStatus == StockTakeStatus.IN_PROCESS_SECOND_STOCK_TAKE) {
                if (isCount) {
                    stockCountItems = stockCountItems.stream()
                            .filter(stockCountItem -> stockCountItem.getSecondQuantity() != null)
                            .collect(Collectors.toList());
                } else {
                    stockCountItems = stockCountItems.stream()
                            .filter(stockCountItem -> stockCountItem.getSecondQuantity() == null)
                            .collect(Collectors.toList());
                }
            }
        }
        if(searchName != null){
            stockCountItems.stream().filter(stockCountItem -> stockCountItem.get)
        }

    }*/

    public StockTake stopCountStockTake(String stockTakeId, List<StockCountItem> stockCountItems) throws CMSException {
        Optional<StockTake> stockTakeOpt = stockTakeDatabaseService.findStockTakeById(stockTakeId);

        if(!stockTakeOpt.isPresent()){
            throw new CMSException(StatusCode.E2000, "Stock take not found");
        }
        StockTake existStockTake = stockTakeOpt.get();

        List<StockCountItem> existStockCountItems = existStockTake.getStockCountItems();

        stockCountItems.forEach(
                stockCountItem -> {
                    Optional<StockCountItem> stockCountItemOpt = existStockCountItems.stream()
                            .filter(existStockCountItem -> existStockCountItem.getId().equals(stockCountItem.getId()))
                            .findFirst();
                    if(stockCountItemOpt.isPresent()){
                        StockCountItem existStockCountItem = stockCountItemOpt.get();
                        existStockCountItem.setBatchNumber(stockCountItemOpt.get().getBatchNumber());
                        existStockCountItem.setExpiryDate(stockCountItemOpt.get().getExpiryDate());
                        existStockCountItem.setFirstQuantity(stockCountItemOpt.get().getFirstQuantity());
                        existStockCountItem.setSecondQuantity(stockCountItemOpt.get().getSecondQuantity());
                    }else{
                        existStockCountItems.add(stockCountItem);
                    }
                }
        );

        if(!existStockTake.getStockCountItems().stream()
                .allMatch(stockCountItem-> stockCountItem.checkValidate(existStockTake.getStockTakeStatus()))){
            throw new CMSException(StatusCode.E1002, "Not valid in stockCountItem");
        }

        StockTake savedStockTake = stockTakeDatabaseService.saveStockTake(existStockTake);
        return savedStockTake;
    }

    public boolean submitStockTakeForReview(String stockTakeId) throws CMSException {
        Optional<StockTake> stockTakeOpt = stockTakeDatabaseService.findStockTakeById(stockTakeId);

        if(!stockTakeOpt.isPresent()){
            throw new CMSException(StatusCode.E2000, "Stock take not found");
        }
        StockTake stockTake = stockTakeOpt.get();
        switch (stockTake.getStockTakeStatus()){
            case IN_PROCESS_FIRST_STOCK_TAKE:
                if (stockTake.getStockCountItems().stream()
                        .allMatch(stockCountItem -> stockCountItem.checkValidate(StockTakeStatus.IN_PROCESS_SECOND_STOCK_TAKE))){
                    stockTake.setStockTakeStatus(StockTakeStatus.IN_PROCESS_SECOND_STOCK_TAKE);
                }else{
                    throw new CMSException(StatusCode.E1010);
                }
                break;
            case IN_PROCESS_SECOND_STOCK_TAKE:
                if (stockTake.getStockCountItems().stream()
                        .allMatch(stockCountItem -> stockCountItem.checkValidate(StockTakeStatus.COMPLETED))){
                    stockTake.setStockTakeStatus(StockTakeStatus.COMPLETED);
                }else{
                    throw new CMSException(StatusCode.E1010);
                }
                break;
        }
        stockTakeDatabaseService.saveStockTake(stockTake);
        return true;
    }

    public Inventory searchInventoryByItemCode(String clinicId, String itemCode, String batchNo) throws CMSException {
        Optional<Item> itemOpt = itemDatabaseService.findByClinicIdAndCode(clinicId, itemCode);
        if(!itemOpt.isPresent()){
            throw new CMSException(StatusCode.E2000, "Item not found");
        }

        Item item = itemOpt.get();
        if(!item.isInventoried()){
            throw new CMSException(StatusCode.E1010, "Not allow item to store inventory");
        }

        Optional<Location> locationOpt = locationDatabaseService.findLocationByClinicId(clinicId);
        if(locationOpt.isPresent()){
            Location location = locationOpt.get();
            Optional<Inventory> inventoryOpt = location.getInventory().stream()
                    .filter(inventory -> inventory.getItemRefId().equals(item.getId())
                            && inventory.getBatchNumber().equals(batchNo))
                    .findFirst();
            if(inventoryOpt.isPresent()){
                inventoryOpt.get().setItemCode(item.getCode());
                inventoryOpt.get().setItemName(item.getName());
                return inventoryOpt.get();
            }
        }
        Inventory inventory = new Inventory(item.getId(), batchNo, item.getBaseUom(), null,
                0, item.getReorderPoint() < 0);
        return inventory;
    }

    public StockTake adjustStockLevel(String clinicId, String countName, String itemCode, String batchNo, String uom,
                                      LocalDate expiryDate, int newStockLevel, String purposeOfAdjustment,
                                      String remark) throws CMSException {
        Optional<Location> locationOpt = locationDatabaseService.findLocationByClinicId(clinicId);
        if(!locationOpt.isPresent()){
            throw new CMSException(StatusCode.E2000, "Location not found");
        }
        Location location = locationOpt.get();

        Optional<Item> itemOpt = itemDatabaseService.findByClinicIdAndCode(clinicId, itemCode);
        if(!itemOpt.isPresent()){
            throw new CMSException(StatusCode.E2000, "Item not found");
        }

        Item item = itemOpt.get();
        Optional<Inventory> inventoryOpt = location.getInventory().stream()
                .filter(inventory -> inventory.getItemRefId().equals(item.getId())
                        && inventory.getBatchNumber().equals(batchNo)).findFirst();
        if(!inventoryOpt.isPresent()){
            throw new CMSException(StatusCode.E2000, "Inventory not found");
        }
        Inventory inventory = inventoryOpt.get();


        StockTake stockTake = new StockTake(generateStockTakeName(clinicId, LocalDate.now(), LocalTime.now()), LocalDate.now(),
                LocalTime.now(), clinicId, StockCountType.ADJUSTMENT, null, countName, StockTakeStatus.COMPLETED,
                StockTakeApproveStatus.NOT_APPROVED);
        double ratio = uomMatrixService.findRatio(uom, item.getBaseUom());
        double count = newStockLevel * ratio;
        StockCountItem stockCountItem = new StockCountItem(inventory.getInventoryId(), item.getCode(), item.getName(),
                item.getId(), inventory.getBatchNumber(), inventory.getBaseUom(),
                inventory.getExpiryDate(), inventory.getAvailableCount()/100);
        stockCountItem.setBatchNumber(batchNo);
        stockCountItem.setExpiryDate(expiryDate);
        stockCountItem.setFirstQuantity(count);
        stockCountItem.setPurposeOfAdjustment(purposeOfAdjustment);
        stockCountItem.setReason(remark);
        stockTake.addStockCountItem(stockCountItem);
        if(!stockTake.checkValidate()){
            throw new CMSException(StatusCode.E1002);
        }
        return stockTakeDatabaseService.saveStockTake(stockTake);
    }


    public boolean approveStockTake(String stockTakeId) throws CMSException {
        Optional<StockTake> stockTakeOpt = stockTakeDatabaseService.findStockTakeById(stockTakeId);
        if(!stockTakeOpt.isPresent()){
            throw new CMSException(StatusCode.E2000, "Stock take not found");
        }
        StockTake stockTake = stockTakeOpt.get();
        if(stockTake.getStockTakeStatus() != StockTakeStatus.COMPLETED){
            throw new CMSException(StatusCode.E1010, "Not allow to approve with status["+stockTake.getStockTakeStatus()+"]");
        }
        stockTake.setApproveStatus(StockTakeApproveStatus.APPROVED);

        Optional<Location> locationOpt = locationDatabaseService.findLocationByClinicId(stockTake.getClinicId());
        Location location = locationOpt.orElse(new Location(stockTake.getClinicId()));
        List<Inventory> inventories = location.getInventory();
        List<StockCountItem> stockCountItems = stockTake.getStockCountItems();

        stockCountItems.forEach(
                stockCountItem -> {
                    Optional<Inventory> inventoryOpt = inventories.stream()
                            .filter(inventory -> inventory.getInventoryId().equals(stockCountItem.getInventoryId()))
                            .findFirst();
                    if(inventoryOpt.isPresent()){
                        inventoryOpt.get().setBatchNumber(stockCountItem.getBatchNumber());
                        inventoryOpt.get().setExpiryDate(stockCountItem.getExpiryDate());
                        if(stockTake.getCountType() == StockCountType.FULL || stockTake.getCountType() == StockCountType.PARTIAL) {
                            inventoryOpt.get().setAvailableCount(roundingQuantity(stockCountItem.getSecondQuantity()));
                        }else if(stockTake.getCountType() == StockCountType.ADJUSTMENT){
                            inventoryOpt.get().setAvailableCount(roundingQuantity(stockCountItem.getFirstQuantity()));
                        }
                    }else{
                        inventories.add(conventStockCountItemToInventory(stockTake.getClinicId(), stockCountItem));
                    }
                }
        );

        //location.setInventory(inventories);
        locationDatabaseService.saveLocation(location);
        stockTakeDatabaseService.saveStockTake(stockTake);
        return true;
    }

    public boolean rejectStockTake(String stockTakeId) throws CMSException {
        Optional<StockTake> stockTakeOpt = stockTakeDatabaseService.findStockTakeById(stockTakeId);
        if(!stockTakeOpt.isPresent()){
            throw new CMSException(StatusCode.E2000, "Stock take not found");
        }
        StockTake stockTake = stockTakeOpt.get();
        if(stockTake.getStockTakeStatus() != StockTakeStatus.COMPLETED){
            throw new CMSException(StatusCode.E1010, "Not allow to reject with status["+stockTake.getStockTakeStatus()+"]");
        }
        stockTake.setApproveStatus(StockTakeApproveStatus.REJECTED);
        stockTakeDatabaseService.saveStockTake(stockTake);
        return true;
    }

    public List<StockTake> findStockTakesByStockTakeStatusAndApproveStatus(StockTakeStatus stockTakeStatus,
                                                                                StockTakeApproveStatus stockTakeApproveStatus, int size){
        return stockTakeDatabaseService.findStockTakeByStockTakeStatusOrApproveStatus(stockTakeStatus, stockTakeApproveStatus,
        size, "startDate", Sort.Direction.ASC);
    }

    private int roundingQuantity(double quantity){
        return (int)((Math.round(quantity * 100) / 100D) * 100);
    }

    private String generateStockTakeName(String clinicId, LocalDate localDate, LocalTime localTime) throws CMSException {
        Optional<Clinic> activeClinicOpt = clinicDatabaseService.findActiveClinic(clinicId);
        if(!activeClinicOpt.isPresent()){
            throw new CMSException(StatusCode.E2000, "Clinic not found");
        }
            return activeClinicOpt.get().getName() + " - " +
                LocalDateTime.of(localDate, localTime).format(DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a"));
    }

    private StockCountItem conventInventoryToStockCountItem(Inventory inventory) throws CMSException {
        Optional<Item> itemOpt = itemDatabaseService.findItemByItemId(inventory.getItemRefId());
        if(!itemOpt.isPresent()){
            throw new CMSException(StatusCode.E2000, "Item not found");
        }
        StockCountItem stockCountItem = new StockCountItem(inventory.getInventoryId(), itemOpt.get().getCode(),
                itemOpt.get().getName(),inventory.getItemRefId(),
                inventory.getBatchNumber(), inventory.getBaseUom(), inventory.getExpiryDate(), inventory.getAvailableCount()/100);
        return stockCountItem;
    }

    private Inventory conventStockCountItemToInventory(String clinicId, StockCountItem stockCountItem){
        Inventory inventory = new Inventory(stockCountItem.getItemRefId(), stockCountItem.getBatchNumber(),
                stockCountItem.getBaseUom(), stockCountItem.getExpiryDate(), roundingQuantity(stockCountItem.getSecondQuantity()), false);
        List<Item> items = itemDatabaseService.findItemByClinicId(clinicId);
        return updateInventoryLowStockLevel(items, inventory);
    }

    private Inventory updateInventoryLowStockLevel(List<Item> items, Inventory inventory){
        Optional<Item> itemOpt = items.stream()
                .filter(item -> item.getId().equals(inventory.getItemRefId()))
                .findFirst();
        if(itemOpt.isPresent()) {
            Item item = itemOpt.get();
            if (item.getReorderPoint() > inventory.getAvailableCount()) {
                inventory.setLowStockLevel(true);
            } else {
                inventory.setLowStockLevel(false);
            }

        }
        return inventory;
    }

    private List<Inventory> prepareDrugInventory(List<Inventory> inventories, List<DrugItem> drugItems, boolean isLowLevel, boolean isExpired) {
        List<String> drugItemIds = drugItems.stream()
                .map(Item::getId)
                .collect(Collectors.toList());

        List<Inventory> searchInventories = inventories.stream()
                .filter(inventory -> drugItemIds.contains(inventory.getItemRefId()))
                .collect(Collectors.toList());
        if (isLowLevel) {
            searchInventories = searchInventories.stream().filter(inventory -> {
                Optional<DrugItem> matchDrugItemOpt = drugItems.stream()
                        .filter(drugItem -> inventory.getItemRefId().equals(drugItem.getId()))
                        .findFirst();
                if (matchDrugItemOpt.isPresent()) {
                    if (matchDrugItemOpt.get().getSafetyStockQty() > inventory.getAvailableCount()) {
                        return true;
                    }
                }
                return false;
            }).collect(Collectors.toList());
        }
        if (isExpired) {
            searchInventories = searchInventories.stream()
                    .filter(inventory -> inventory.getExpiryDate().isAfter(LocalDate.now()))
                    .collect(Collectors.toList());
        }

        searchInventories.forEach(
                inventory -> {
                    Optional<DrugItem> drugItemOpt = drugItems.stream()
                            .filter(drugItem -> inventory.getItemRefId().equals(drugItem.getId()))
                            .findFirst();
                    if (drugItemOpt.isPresent()) {
                        inventory.setItemName(drugItemOpt.get().getName());
                        inventory.setItemCode(drugItemOpt.get().getCode());
                    }
                }
        );

        searchInventories = searchInventories.stream()
                .sorted(Comparator.comparing(Inventory::getItemName))
                .collect(Collectors.toList());
        return searchInventories;
    }

}
