package com.ilt.cms.inventory.service.purchase;

import com.ilt.cms.core.entity.item.DrugItem;
import com.ilt.cms.database.RunningNumberService;
import com.ilt.cms.inventory.db.service.interfaces.OrderDatabaseService;
import com.ilt.cms.inventory.db.service.interfaces.UOMMatrixDatabaseService;
import com.ilt.cms.inventory.model.common.UOM;
import com.ilt.cms.inventory.model.inventory.Inventory;
import com.ilt.cms.inventory.model.inventory.Location;
import com.ilt.cms.inventory.model.purchase.*;
import com.ilt.cms.inventory.model.purchase.enums.OrderStatus;
import com.ilt.cms.inventory.model.purchase.enums.RequestStatus;
import com.ilt.cms.inventory.service.common.UOMMatrixService;
import com.ilt.cms.inventory.service.inventory.DrugItemService;
import com.ilt.cms.inventory.service.inventory.LocationService;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.CommonUtils;
import com.lippo.commons.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PurchaseService {
    private static final Logger logger = LoggerFactory.getLogger(PurchaseService.class);
    private OrderDatabaseService orderDatabaseService;
    private RunningNumberService runningNumberService;
    private LocationService locationService;
    private UOMMatrixService uomMatrixService;
    private DrugItemService drugItemService;

    public PurchaseService(OrderDatabaseService orderDatabaseService, RunningNumberService runningNumberService,
                           LocationService locationService, UOMMatrixService uomMatrixService,
                           DrugItemService drugItemService){
        this.orderDatabaseService = orderDatabaseService;
        this.runningNumberService = runningNumberService;
        this.locationService = locationService;
        this.uomMatrixService = uomMatrixService;
        this.drugItemService = drugItemService;
    }

    public List<PurchaseRequest> listPurchaseRequest(){
        List<Request> requests = orderDatabaseService.findPurchaseRequest();
        return requests.stream().map(request -> (PurchaseRequest)request).collect(Collectors.toList());
    }

    public List<PurchaseOrder> listPurchaseOrder(){
        List<Order> orders = orderDatabaseService.findPurchaseOrder();
        return orders.stream().map(order -> (PurchaseOrder) order).collect(Collectors.toList());
    }

    public List<PurchaseRequest> listPurchaseRequestByClinicId(String clinicId){
        List<Request> requests = orderDatabaseService.findPurchaseRequestByClinicId(clinicId);
        return requests.stream().map(request -> (PurchaseRequest)request).collect(Collectors.toList());
    }

    public List<PurchaseOrder> listPurchaseOrderByClinicId(String clinicId){
        List<Order> orders = orderDatabaseService.findPurchaseOrderByClinicId(clinicId);
        return orders.stream().map(order -> (PurchaseOrder) order).collect(Collectors.toList());
    }

    public PurchaseRequest createPurchaseRequest(PurchaseRequest purchaseRequest) throws CMSException {
        if(!purchaseRequest.checkValidate()){
            throw new CMSException(StatusCode.E1002);
        }

        if(purchaseRequest.getRequestNo() == null){
            purchaseRequest.setRequestNo(runningNumberService.generateRequestNumber());
        }

        if(purchaseRequest.getRequestStatus() == RequestStatus.REQUESTED){
            List<DrugItem> drugItems = drugItemService.listDrugItemByClinicId(purchaseRequest.getRequestClinicId());
            boolean allMatch = purchaseRequest.getRequestItems().stream()
                    .allMatch(requestItem ->  checkDrugItemIdExist(drugItems, requestItem.getItemRefId()));
            if(!allMatch){
                throw new CMSException(StatusCode.E2000, "Drug item not found");
            }
        }
        if(purchaseRequest.getRequestStatus() != RequestStatus.DRAFT && purchaseRequest.getRequestNo() == null) {
            purchaseRequest.setRequestTime(LocalDateTime.now());
        }
        return orderDatabaseService.savePurchaseRequest(purchaseRequest);
    }

    public PurchaseRequest modifyPurchaseRequest(String purchaseRequestId, PurchaseRequest purchaseRequest) throws CMSException {
        if(!purchaseRequest.checkValidate()){
            throw new CMSException(StatusCode.E1002);
        }

        Optional<PurchaseRequest> curPurchaseRequestOpt = orderDatabaseService.findPurchaseRequestById(purchaseRequestId);

        if(!curPurchaseRequestOpt.isPresent()){
            throw new CMSException(StatusCode.E2000, "Purchase request not found");
        }

        PurchaseRequest curPurchaseRequest = curPurchaseRequestOpt.get();

        if(curPurchaseRequest.getRequestStatus() !=  RequestStatus.DRAFT){
            throw new CMSException(StatusCode.E1010, "purchaseRequest is requested");
        }

        if(purchaseRequest.getRequestStatus() == RequestStatus.REQUESTED){
            List<DrugItem> drugItems = drugItemService.listDrugItemByClinicId(purchaseRequest.getRequestClinicId());
            boolean allMatch = purchaseRequest.getRequestItems().stream()
                    .allMatch(requestItem ->  checkDrugItemIdExist(drugItems, requestItem.getItemRefId()));
            if(!allMatch){
                throw new CMSException(StatusCode.E2000, "Drug item not found");
            }
        }

        if(purchaseRequest.getRequestStatus() != RequestStatus.DRAFT) {
            curPurchaseRequest.setRequestTime(LocalDateTime.now());
        }
        curPurchaseRequest.setRequestStatus(purchaseRequest.getRequestStatus());
        curPurchaseRequest.setRequestItems(purchaseRequest.getRequestItems());

        return orderDatabaseService.savePurchaseRequest(curPurchaseRequest);
    }

    public boolean approvePurchaseRequest(String purchaseRequestId) throws CMSException {
        Optional<PurchaseRequest> curPurchaseRequestOpt = orderDatabaseService.findPurchaseRequestById(purchaseRequestId);

        if(!curPurchaseRequestOpt.isPresent()){
            throw new CMSException(StatusCode.E2000, "Purchase request not found");
        }

        PurchaseRequest curPurchaseRequest = curPurchaseRequestOpt.get();

        if(curPurchaseRequest.getRequestStatus() !=  RequestStatus.REQUESTED){
            throw new CMSException(StatusCode.E1010, "Modify purchase request not allow:" + curPurchaseRequest.getRequestStatus());
        }
        if(!curPurchaseRequest.checkValidate()){
            throw new CMSException(StatusCode.E1002, "Invalid in purchase request");
        }
        curPurchaseRequest.setRequestStatus(RequestStatus.APPROVED);
        createPurchaseOrderByRequestId(purchaseRequestId);
        orderDatabaseService.savePurchaseRequest(curPurchaseRequest);
        return true;
    }

    public boolean rejectPurchaseRequest(String purchaseRequestId) throws CMSException {
        Optional<PurchaseRequest> curPurchaseRequestOpt = orderDatabaseService.findPurchaseRequestById(purchaseRequestId);

        if(!curPurchaseRequestOpt.isPresent()){
            throw new CMSException(StatusCode.E2000, "Transfer request not found");
        }

        PurchaseRequest curPurchaseRequest = curPurchaseRequestOpt.get();
        if(curPurchaseRequest.getRequestStatus() !=  RequestStatus.REQUESTED){
            throw new CMSException(StatusCode.E1010, "Modify transfer request not allow:" + curPurchaseRequest.getRequestStatus());
        }
        curPurchaseRequest.setRequestStatus(RequestStatus.REJECTED);
        orderDatabaseService.savePurchaseRequest(curPurchaseRequest);
        return true;

    }

    public List<PurchaseOrder> createPurchaseOrderByRequestId(String requestId) throws CMSException {
        Optional<PurchaseRequest> purchaseRequestOpt = orderDatabaseService.findPurchaseRequestById(requestId);
        if(!purchaseRequestOpt.isPresent()){
            throw new CMSException(StatusCode.E2000, "Purchase request["+requestId+"] not found");
        }
        PurchaseRequest purchaseRequest = purchaseRequestOpt.get();
        if(purchaseRequest.getRequestStatus() != RequestStatus.REQUESTED){
            throw new CMSException(StatusCode.E1010, "Invalid request status");
        }

        return createPurchaseOrderByPurchaseRequest(purchaseRequestOpt.get());
    }

    private PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder) throws CMSException {
        if(!purchaseOrder.checkValidate()){
            throw new CMSException(StatusCode.E1002, "Invalid in purchase order");
        }
        if(!purchaseOrder.getGoodPurchasedItems().stream().allMatch(GoodOrderedItem::checkValidate)){
            throw new CMSException(StatusCode.E1002, "Invalid in good purchased items");
        }

        if(!purchaseOrder.getGoodReceiveNotes().stream()
                .flatMap(goodReceiveNote -> goodReceiveNote.getReceivedItems().stream())
                .allMatch(GoodReceivedItem::checkValidate)){
            throw new CMSException(StatusCode.E1002, "Invalid in good received item");
        }

        if(purchaseOrder.getOrderStatus() == OrderStatus.REJECTED
                || purchaseOrder.getOrderStatus() == OrderStatus.COMPLETED){
            throw new CMSException(StatusCode.E1002, "Invalid order status");
        }

        if(purchaseOrder.getOrderNo() == null){
            purchaseOrder.setOrderNo(runningNumberService.generateOrderNumber());
        }

        return orderDatabaseService.savePurchaseOrder(purchaseOrder);
    }

    public GoodReceiveNote addGoodReceiveNote(String purchaseOrderId, GoodReceiveNote goodReceiveNote) throws CMSException {
        if(!goodReceiveNote.checkValidate()){
            throw new CMSException(StatusCode.E1002, "Invalid in good receive note");
        }

        if(!goodReceiveNote.getReceivedItems().stream()
                .allMatch(goodReceivedItem -> goodReceivedItem.checkValidate())){
            throw new CMSException(StatusCode.E1002, "Invalid in good receive Item");
        }

        Optional<PurchaseOrder> curPurchaseOrderOpt = orderDatabaseService.findPurchaseOrderById(purchaseOrderId);

        if(!curPurchaseOrderOpt.isPresent()){
            throw new CMSException(StatusCode.E2000, "Order not found");
        }

        PurchaseOrder curPurchaseOrder = curPurchaseOrderOpt.get();

        if(curPurchaseOrder.getOrderStatus() != OrderStatus.APPROVED){
            throw new CMSException(StatusCode.E1010, "Invalid order status:" + curPurchaseOrder.getOrderStatus());
        }

        if(goodReceiveNote.getGrnNo() == null){
            goodReceiveNote.setGrnNo(runningNumberService.generateGRNNumber());
        }
        String grnNo = goodReceiveNote.getGrnNo();

        goodReceiveNote.setCreateDate(LocalDate.now());

        curPurchaseOrder.addGoodReceiveNote(goodReceiveNote);
        //prepare rollback data
        Location backupLocation = locationService.findLocationByClinicId(curPurchaseOrder.getRequestClinicId());
        Optional<PurchaseOrder> backupPurchaseOrderOpt = orderDatabaseService.findPurchaseOrderById(purchaseOrderId);

        if (checkIsAllItemReceived(curPurchaseOrder)) {
            if (curPurchaseOrder.getOrderStatus() != OrderStatus.REJECTED) {
                curPurchaseOrder.setOrderStatus(OrderStatus.COMPLETED);
                if (curPurchaseOrder.getCompleteTime() == null) {
                    curPurchaseOrder.setCompleteTime(LocalDateTime.now());
                }
            }
        }
        try{
            logger.info("update inventory");
            curPurchaseOrder = updateRequestClinicStockInventory(curPurchaseOrder);

            logger.info("save purchase order");
            PurchaseOrder savePurchaseOrder = orderDatabaseService.savePurchaseOrder(curPurchaseOrder);
            Optional<GoodReceiveNote> saveGoodReceiveNoteOpt = savePurchaseOrder.getGoodReceiveNotes().stream()
                    .filter(goodReceiveNote1 -> goodReceiveNote1.getGrnNo().equals(grnNo))
                    .findFirst();
            if(saveGoodReceiveNoteOpt.isPresent()){
                return saveGoodReceiveNoteOpt.get();
            }
            return null;
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            logger.info("rollback data");
            if(backupLocation != null) {
                locationService.saveLocation(backupLocation);
                logger.info("rollback location completed");
            }
            orderDatabaseService.savePurchaseOrder(backupPurchaseOrderOpt.get());
            logger.info("rollback PurchaseOrder completed");
            throw e;

        }


    }

    public GoodReceiveVoidNote addGoodReceiveVoidNote(String purchaseOrderId, GoodReceiveVoidNote goodReceiveVoidNote) throws CMSException {
        if(!goodReceiveVoidNote.checkValidate()){
            throw new CMSException(StatusCode.E1002, "Invalid in good receive void note");
        }

        if(!goodReceiveVoidNote.getReturnItems().stream()
                .allMatch(returnItem -> returnItem.checkValidate())){
            throw new CMSException(StatusCode.E1002, "Invalid in good return Item");
        }

        Optional<PurchaseOrder> curPurchaseOrderOpt = orderDatabaseService.findPurchaseOrderById(purchaseOrderId);

        if(!curPurchaseOrderOpt.isPresent()){
            throw new CMSException(StatusCode.E2000, "Order not found");
        }

        PurchaseOrder curPurchaseOrder = curPurchaseOrderOpt.get();

        if(curPurchaseOrder.getOrderStatus() != OrderStatus.APPROVED){
            throw new CMSException(StatusCode.E1010, "Invalid order status:" + curPurchaseOrder.getOrderStatus());
        }

        List<GoodReceivedItem> curGoodReceivedItems = curPurchaseOrder.getGoodReceiveNotes().stream()
                .flatMap(goodReceiveNote -> goodReceiveNote.getReceivedItems().stream())
                .collect(Collectors.toList());
        goodReceiveVoidNote.getReturnItems().forEach(
                returnItem -> {
                    Optional<GoodReceivedItem> receivedItemOpt = curGoodReceivedItems.stream()
                            .filter(goodReceivedItem -> goodReceivedItem.getItemRefId().equals(returnItem.getItemRefId()) &&
                                    goodReceivedItem.getBatchNumber().equals(returnItem.getBatchNumber())).findFirst();
                    if(!receivedItemOpt.isPresent()){
                        throw new RuntimeException(new CMSException(StatusCode.E2000, "Return item not found"));
                    }
                    GoodReceivedItem goodReceivedItem = receivedItemOpt.get();
                    String baseUom;
                    try {
                        DrugItem drugItem = drugItemService.findDrugItemById(goodReceivedItem.getItemRefId());
                        baseUom = drugItem.getBaseUom();
                    } catch (CMSException e) {
                        logger.error(e.getMessage(), e);
                        throw new RuntimeException(e);
                    }

                    try {
                        double goodReceiveItemRatio = uomMatrixService.findRatio(goodReceivedItem.getUom(), baseUom);
                        double returnItemRatio = uomMatrixService.findRatio(returnItem.getUom(), baseUom);
                        if(goodReceivedItem.getQuantity() * goodReceiveItemRatio < returnItem.getQuantity() * returnItemRatio){
                            throw new RuntimeException(new CMSException(StatusCode.E1010, "Return quantity should not more than received"));
                        }
                    } catch (CMSException e) {
                        logger.error(e.getMessage(), e);
                        throw new RuntimeException(e);
                    }

                }
        );


        if(goodReceiveVoidNote.getGrnVoidNo() == null){
            goodReceiveVoidNote.setGrnVoidNo(runningNumberService.generateGRVNNumber());
        }
        goodReceiveVoidNote.setCreateDate(LocalDate.now());

        String grnVoidNo = goodReceiveVoidNote.getGrnVoidNo();

        curPurchaseOrder.addGoodReceiveVoidNote(goodReceiveVoidNote);
        //prepare rollback data
        Location backupLocation = locationService.findLocationByClinicId(curPurchaseOrder.getRequestClinicId());
        Optional<PurchaseOrder> backupPurchaseOrderOpt = orderDatabaseService.findPurchaseOrderById(purchaseOrderId);

        try{
            logger.info("update inventory");
            curPurchaseOrder = updateRequestClinicStockInventory(curPurchaseOrder);

            logger.info("save purchase order");
            PurchaseOrder savePurchaseOrder = orderDatabaseService.savePurchaseOrder(curPurchaseOrder);
            Optional<GoodReceiveVoidNote> saveGoodReceiveVoidNoteOpt = savePurchaseOrder.getGoodReceiveVoidNotes().stream()
                    .filter(goodReceiveVoidNote1 -> goodReceiveVoidNote1.getGrnVoidNo().equals(grnVoidNo))
                    .findFirst();
            if(saveGoodReceiveVoidNoteOpt.isPresent()){
                return saveGoodReceiveVoidNoteOpt.get();
            }
            return null;
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            logger.info("rollback data");
            if(backupLocation != null) {
                locationService.saveLocation(backupLocation);
                logger.info("rollback location completed");
            }
            orderDatabaseService.savePurchaseOrder(backupPurchaseOrderOpt.get());
            logger.info("rollback PurchaseOrder completed");
            throw e;

        }
    }

    private boolean checkIsAllItemReceived(PurchaseOrder purchaseOrder){
        List<GoodReceivedItem> goodReceivedItems = purchaseOrder.getGoodReceiveNotes().stream()
                .flatMap(goodReceiveNote -> goodReceiveNote.getReceivedItems().stream())
                .collect(Collectors.toList());

        List<GoodReturnItem> goodReturnItems = purchaseOrder.getGoodReceiveVoidNotes().stream()
                .flatMap(goodReceiveVoidNote -> goodReceiveVoidNote.getReturnItems().stream())
                .collect(Collectors.toList());

        Map<String, Double> itemIdReceiveQtyMap = goodReceivedItems.stream().collect(
                Collectors.groupingBy(GoodReceivedItem::getItemRefId, Collectors.summingDouble(good -> {
                    UOM baseUom = null;
                    try {
                        baseUom = locationService.findBaseUomByItemId(good.getItemRefId());
                    } catch (CMSException e) {
                        logger.error(e.getMessage(), e);
                        throw new RuntimeException(e);
                    }
                    Double ratio = null;
                    try {
                        ratio = uomMatrixService.findRatio(good.getUom(), baseUom.getCode());
                    } catch (CMSException e) {
                        logger.error(e.getMessage(), e);
                        throw new RuntimeException(e);
                    }
                    return ratio * good.getQuantity();
                }))
        );

        Map<String, Double> itemIdReturnQtyMap = goodReturnItems.stream().collect(
                Collectors.groupingBy(GoodReturnItem::getItemRefId, Collectors.summingDouble(good -> {
                    UOM baseUom = null;
                    try {
                        baseUom = locationService.findBaseUomByItemId(good.getItemRefId());
                    } catch (CMSException e) {
                        logger.error(e.getMessage(), e);
                        throw new RuntimeException(e);
                    }
                    Double ratio = null;
                    try {
                        ratio = uomMatrixService.findRatio(good.getUom(), baseUom.getCode());
                    } catch (CMSException e) {
                        logger.error(e.getMessage(), e);
                        throw new RuntimeException(e);
                    }
                    return ratio * good.getQuantity();
                }))
        );

        Map<String, Double> itemIdQtyMap = itemIdReceiveQtyMap.entrySet().stream()
                .collect(Collectors.toMap(map-> map.getKey(), map->(
                    map.getValue() - (itemIdReturnQtyMap.get(map.getKey()) == null ? 0: itemIdReturnQtyMap.get(map.getKey()))
        )));

        boolean isAllReceived = purchaseOrder.getGoodPurchasedItems().stream().allMatch(goodOrderedItem ->{
            UOM baseUom = null;
            Double receivedQty = itemIdQtyMap.get(goodOrderedItem.getItemRefId());
            if(receivedQty == null){
                logger.info("no received quantity for item id:" + goodOrderedItem.getItemRefId());
                receivedQty = 0.0;
            }
            try {
                baseUom = locationService.findBaseUomByItemId(goodOrderedItem.getItemRefId());
            } catch (CMSException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
            Double ratio = null;
            try {
                ratio = uomMatrixService.findRatio(goodOrderedItem.getUom(), baseUom.getCode());
                logger.info("UOM ratio["+ratio+"]");
            } catch (CMSException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }

            logger.info("Compare received item ["+goodOrderedItem.getItemRefId()+"] received qty["+receivedQty+
                    "], ordered qty["+(ratio * goodOrderedItem.getQuantity())+"]");
            if(ratio * goodOrderedItem.getQuantity() <= receivedQty){
                return true;
            }
            return false;
        });

        return isAllReceived;
    }

    private PurchaseOrder updateRequestClinicStockInventory(PurchaseOrder purchaseOrder) throws CMSException {
        logger.info("updateRequestClinicStockInventory");
        //init array
        List<Inventory> addInventory = new ArrayList<>();
        List<Inventory> removeInventory = new ArrayList<>();
        List<GoodReceivedItem> existGoodReceivedItems = new ArrayList<>();
        List<GoodReturnItem> existGoodReturnItems = new ArrayList<>();
        List<DrugItem> drugItems = drugItemService.listDrugItemByClinicId(purchaseOrder.getRequestClinicId());
        //check get exist purchaseOrder
        Optional<PurchaseOrder> curPurchaseOrderOpt;
        if(purchaseOrder.getId() != null) {
            curPurchaseOrderOpt = orderDatabaseService.findPurchaseOrderById(purchaseOrder.getId());
            if(curPurchaseOrderOpt.isPresent()){
                existGoodReceivedItems = curPurchaseOrderOpt.get().getGoodReceiveNotes().stream()
                        .flatMap(goodReceiveNote -> goodReceiveNote.getReceivedItems().stream())
                        .collect(Collectors.toList());
                existGoodReturnItems = curPurchaseOrderOpt.get().getGoodReceiveVoidNotes().stream()
                        .flatMap(goodReceiveVoidNote -> goodReceiveVoidNote.getReturnItems().stream())
                        .collect(Collectors.toList());
            }
        }

        List<GoodReceivedItem> goodReceivedItems = purchaseOrder.getGoodReceiveNotes().stream()
                .flatMap(goodReceiveNote -> goodReceiveNote.getReceivedItems().stream())
                .collect(Collectors.toList());

        addDeliveryItemToInventoryList(goodReceivedItems, existGoodReceivedItems,
                addInventory, removeInventory, drugItems);

        List<GoodReturnItem> goodReturnItems = purchaseOrder.getGoodReceiveVoidNotes().stream()
                .flatMap(goodReceiveVoidNote -> goodReceiveVoidNote.getReturnItems().stream())
                .collect(Collectors.toList());
        addDeliveryVoidItemToInventoryList(goodReturnItems, existGoodReturnItems,
                addInventory, removeInventory, drugItems);

        adjustInventory(purchaseOrder.getRequestClinicId(), addInventory, removeInventory);

        logger.info("set flag for counted item");
        purchaseOrder.getGoodReceiveNotes()
                .forEach(goodReceiveNote -> {
                    goodReceiveNote.getReceivedItems()
                            .forEach(
                                    goodReceivedItem -> {
                                        if(goodReceivedItem.getId() == null) {
                                            goodReceivedItem.setId(CommonUtils.idGenerator());
                                        }
                                        if(checkItemNeedUpdateInventory(drugItems, goodReceivedItem)) {
                                            goodReceivedItem.setCountInStock(true);
                                        }else{
                                            goodReceivedItem.setCountInStock(false);
                                        }
                                    }
                            );
                });

        purchaseOrder.getGoodReceiveVoidNotes()
                .forEach(goodReceiveVoidNote -> {
                    goodReceiveVoidNote.getReturnItems()
                            .forEach(
                                    goodReturnItem -> {
                                        if(goodReturnItem.getId() == null) {
                                            goodReturnItem.setId(CommonUtils.idGenerator());
                                        }
                                        if(checkItemNeedUpdateInventory(drugItems, goodReturnItem)) {
                                            goodReturnItem.setCountInStock(true);
                                        }else{
                                            goodReturnItem.setCountInStock(false);
                                        }
                                    }
                            );
                });
        return purchaseOrder;
    }

    private int roundingQuantity(double quantity){
        return (int)((Math.round(quantity * 100) / 100D) * 100);
    }

    private Inventory convertToInventory(DeliveryItem deliveryItem) throws CMSException {
        UOM baseUom = null;
        try {
            baseUom = locationService.findBaseUomByItemId(deliveryItem.getItemRefId());
        } catch (CMSException e) {
            logger.error(e.getMessage(), e);
            throw new CMSException(e);
        }
        Double ratio = null;
        try {
            ratio = uomMatrixService.findRatio(deliveryItem.getUom(), baseUom.getCode());
        } catch (CMSException e) {
            logger.error(e.getMessage(), e);
            throw new CMSException(e);
        }
        Inventory inventory = new Inventory(deliveryItem.getItemRefId(),
                deliveryItem.getBatchNumber(),
                baseUom.getCode(),
                deliveryItem.getExpiryDate(),
                roundingQuantity(ratio * deliveryItem.getQuantity()),
                false);
        return inventory;
    }

    private Location adjustInventory(String clinicId, List<Inventory> addInventory, List<Inventory> removeInventory) throws CMSException {
        logger.info("Adjust inventory with clinic["+clinicId+"] and add inventory["+addInventory+"], remove inventory ["+removeInventory+"]");
        Location location = locationService.findLocationByClinicId(clinicId);
        List<DrugItem> drugItems = drugItemService.listDrugItemByClinicId(clinicId);
        List<Inventory> curInventories = new ArrayList<>();
        curInventories.addAll(location.getInventory());

        addInventory
                .forEach(inventory -> {
                    Optional<Inventory> matchInventoryOpt = curInventories.stream()
                        .filter(curInventory -> curInventory.getInventoryId().equals(inventory.getInventoryId()))
                        .findFirst();
                    if(matchInventoryOpt.isPresent()){
                        matchInventoryOpt.get().setAvailableCount(matchInventoryOpt.get().getAvailableCount() + inventory.getAvailableCount());
                        matchInventoryOpt.get().setExpiryDate(inventory.getExpiryDate());
                        logger.debug("Available count from clinic["+clinicId+"], itemRefId["+matchInventoryOpt.get().getItemRefId()
                                +"], batchNo["+matchInventoryOpt.get().getBatchNumber()+"] update to [" + matchInventoryOpt.get().getAvailableCount() + "]");
                    }else{
                        curInventories.add(inventory);
                        logger.debug("Available count from clinic["+clinicId+"], itemRefId["+inventory.getItemRefId()
                                +"], batchNo["+inventory.getBatchNumber()+"] update to [" + inventory.getAvailableCount() + "]");
                    }

        });
        removeInventory.stream()
                .forEach(inventory -> {
                    Optional<Inventory> matchInventoryOpt = curInventories.stream()
                            .filter(curInventory -> curInventory.getInventoryId().equals(inventory.getInventoryId()))
                            .findFirst();

                    if(matchInventoryOpt.isPresent()){
                        matchInventoryOpt.get().setAvailableCount(matchInventoryOpt.get().getAvailableCount() - inventory.getAvailableCount());
                        matchInventoryOpt.get().setExpiryDate(inventory.getExpiryDate());
                        logger.debug("Available count from clinic["+clinicId+"], itemRefId["+matchInventoryOpt.get().getItemRefId()
                                +"], batchNo["+matchInventoryOpt.get().getBatchNumber()+"] update to [" + matchInventoryOpt.get().getAvailableCount() + "]");
                    }else{
                        inventory.setAvailableCount(0 - inventory.getAvailableCount());
                        logger.debug("Available count from clinic["+clinicId+"], itemRefId["+inventory.getItemRefId()
                                +"], batchNo["+inventory.getBatchNumber()+"] update to [" + inventory.getAvailableCount() + "]");

                        curInventories.add(inventory);
                    }
                });

        boolean isMatch = curInventories.stream().anyMatch(
                inventory -> {
                    try {
                        DrugItem drugItem = drugItemService.findDrugItemById(inventory.getItemRefId());
                        if(!drugItem.isAllowNegativeInventory()){
                            if(inventory.getAvailableCount() < 0){
                                logger.info("item["+inventory.getItemRefId()+"] inventory available count should not be negative");
                                return true;
                            }
                        }
                        return false;
                    } catch (CMSException e) {
                        logger.error(e.getMessage(), e);
                    }
                    return false;
                }
        );
        if(isMatch){
            throw new CMSException(StatusCode.E1010, "Inventory available count not allow negative");
        }
        List<Inventory> updatedInventories = curInventories.stream()
                .map(inventory ->updateInventoryLowStockLevel(drugItems, inventory))
                .collect(Collectors.toList());
        logger.debug("curInventories:" + updatedInventories);
        location.setInventory(updatedInventories);
        if (location.getId() != null) {
            location = locationService.modifyLocation(location.getId(), location);
        } else {
            location = locationService.saveLocation(location);
        }
        return location;
    }

    private List<PurchaseOrder> createPurchaseOrderByPurchaseRequest(PurchaseRequest purchaseRequest) {
        purchaseRequest.getRequestItems().forEach(
                requestItem -> {
                    if (requestItem.getSupplierId() == null){
                        requestItem.setSupplierId(purchaseRequest.getSupplierId());
                    }
                }
        );
        Map<String, List<RequestItem>> supplierIdRequestItemMap = purchaseRequest.getRequestItems().stream()
                .collect(Collectors.groupingBy(RequestItem::getSupplierId));
        List<PurchaseOrder> purchaseOrders = supplierIdRequestItemMap.entrySet().stream()
                .map(stringListEntry ->
                        createPurchaseOrderByRequestItems(purchaseRequest.getRequestClinicId(), stringListEntry.getKey(),
                                purchaseRequest.getId(), stringListEntry.getValue())).collect(Collectors.toList());

        List<PurchaseOrder> savedPurchaseOrders = new ArrayList<>();
        purchaseOrders.forEach(
                purchaseOrder -> {
                    try {
                        savedPurchaseOrders.add(createPurchaseOrder(purchaseOrder));
                    } catch (CMSException e) {
                        logger.error(e.getMessage(), e);
                        throw new RuntimeException(e);
                    }
                }
        );
        return savedPurchaseOrders;
    }

    private PurchaseOrder createPurchaseOrderByRequestItems(String requestClinicId, String supplierId, String requestId, List<RequestItem> requestItems) {
        PurchaseOrder purchaseOrder = new PurchaseOrder(requestId, requestClinicId,
                null, LocalDateTime.now(), null, supplierId, OrderStatus.APPROVED);
        List<GoodOrderedItem> goodOrderItems = requestItems.stream()
                .map(this::mapRequestItemToGoodOrderedItem)
                .collect(Collectors.toList());
        purchaseOrder.setGoodPurchasedItems(goodOrderItems);
        return purchaseOrder;
    }

    private GoodOrderedItem mapRequestItemToGoodOrderedItem(RequestItem requestItem){
        GoodOrderedItem goodOrderedItem = new GoodOrderedItem(requestItem.getItemRefId(), requestItem.getUom(),
                requestItem.getQuantity(), requestItem.getUnitPrice());
        return goodOrderedItem;
    }

    private boolean checkDrugItemIdExist(List<DrugItem> drugItems, String itemRefId){
        return drugItems.stream()
                .anyMatch(drugItem -> drugItem.getId().equals(itemRefId));
    }

    private boolean checkItemNeedUpdateInventory(List<DrugItem> drugItems, DeliveryItem deliveryItem){
        Optional<DrugItem> drugItemOpt = drugItems.stream()
                .filter(drugItem -> drugItem.getId().equals(deliveryItem.getItemRefId()))
                .findFirst();
        if(drugItemOpt.isPresent()){
            DrugItem drugItem = drugItemOpt.get();
            logger.debug("drugItem["+drugItem.getId()+"] need inventory["+drugItem.isInventoried()+"]");
            return drugItem.isInventoried();
        }else{
            logger.error("Item["+deliveryItem.getItemRefId()+"] not found");
        }
        return false;
    }

    private Inventory updateInventoryLowStockLevel(List<DrugItem> drugItems, Inventory inventory){
        Optional<DrugItem> drugItemOpt = drugItems.stream()
                .filter(drugItem -> drugItem.getId().equals(inventory.getItemRefId()))
                .findFirst();
        if(drugItemOpt.isPresent()) {
            DrugItem drugItem = drugItemOpt.get();
            if (drugItem.getSafetyStockQty() > inventory.getAvailableCount()) {
                inventory.setLowStockLevel(true);
            } else {
                inventory.setLowStockLevel(false);
            }

        }
        return inventory;
    }

    private void addDeliveryItemToInventoryList(List<? extends DeliveryItem> deliveryItems,
                                                List<? extends DeliveryItem> existingDeliveryItems,
                                                List<Inventory> addInventories,
                                                List<Inventory> removeInventories,
                                                List<DrugItem> drugItems){
        logger.info("addDeliveryItemToInventoryList, deliveryItems size["+deliveryItems+"] " +
                "and existingDeliveryItems size["+existingDeliveryItems+"]");
        List<Inventory> newAddInventory = deliveryItems.stream()
                .filter(deliveryItem -> deliveryItem.getId() == null && checkItemNeedUpdateInventory(drugItems, deliveryItem))
                .map(deliveryItem -> {
                    try {
                        logger.debug("Add new item[" + deliveryItem.getItemRefId() + "] and batch No["
                                + deliveryItem.getBatchNumber() + "] that UOM["
                                + deliveryItem.getUom() + "] and Qty["
                                + deliveryItem.getQuantity() + "]");
                        return convertToInventory(deliveryItem);
                    } catch (CMSException e) {
                        logger.error(e.getMessage(), e);
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        logger.debug("newAddInventory["+newAddInventory+"]");
        addInventories.addAll(newAddInventory);

        existingDeliveryItems.forEach(
                existDeliveryItem -> {
                    Optional<? extends DeliveryItem> matchItemOpt = deliveryItems.stream()
                            .filter(deliveryItem -> deliveryItem.isSameItem(existDeliveryItem))
                            .findFirst();
                    if (!matchItemOpt.isPresent()) {
                        logger.debug("Remove missing old item[" + existDeliveryItem.getItemRefId() + "] and batch No["
                                + existDeliveryItem.getBatchNumber() + "] that UOM["
                                + existDeliveryItem.getUom() + "] and Qty["
                                + existDeliveryItem.getQuantity() + "]");
                        try {
                            if(checkItemNeedUpdateInventory(drugItems, existDeliveryItem)) {
                                removeInventories.add(convertToInventory(existDeliveryItem));
                            }
                        } catch (CMSException e) {
                            logger.error(e.getMessage(), e);
                            throw new RuntimeException(e);
                        }
                    }else{
                        if(!(matchItemOpt.get().isSameItem(existDeliveryItem))){
                            logger.debug("Remove changed item[" + existDeliveryItem.getItemRefId() + "] and batch No["
                                    + existDeliveryItem.getBatchNumber() + "] that UOM["
                                    + existDeliveryItem.getUom() + "] and Qty["
                                    + existDeliveryItem.getQuantity() + "]");
                            logger.debug("Add changed item[" + matchItemOpt.get().getItemRefId() + "] and batch No["
                                    + matchItemOpt.get().getBatchNumber() + "] that UOM["
                                    + matchItemOpt.get().getUom() + "] and Qty["
                                    + matchItemOpt.get().getQuantity() + "]");
                            try {
                                if(checkItemNeedUpdateInventory(drugItems, existDeliveryItem)) {
                                    removeInventories.add(convertToInventory(existDeliveryItem));
                                }
                                if(checkItemNeedUpdateInventory(drugItems, existDeliveryItem)) {
                                    addInventories.add(convertToInventory(matchItemOpt.get()));
                                }
                            } catch (CMSException e) {
                                logger.error(e.getMessage(), e);
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
        );

    }

    private void addDeliveryVoidItemToInventoryList(List<? extends DeliveryItem> deliveryVoidItems,
                                                    List<? extends DeliveryItem> existingDeliveryVoidItems,
                                                    List<Inventory> addInventories,
                                                    List<Inventory> removeInventories,
                                                    List<DrugItem> drugItems ){
        logger.info("addDeliveryVoidItemToInventoryList, deliveryItems size["+deliveryVoidItems.size()+"] " +
                "and existingDeliveryItems size["+existingDeliveryVoidItems.size()+"]");
        List<Inventory> newAddInventory = deliveryVoidItems.stream()
                .filter(deliveryItem -> deliveryItem.getId() == null && checkItemNeedUpdateInventory(drugItems, deliveryItem))
                .map(deliveryItem -> {
                    try {
                        logger.debug("Add new item[" + deliveryItem.getItemRefId() + "] and batch No["
                                + deliveryItem.getBatchNumber() + "] that UOM["
                                + deliveryItem.getUom() + "] and Qty["
                                + deliveryItem.getQuantity() + "]");
                        return convertToInventory(deliveryItem);
                    } catch (CMSException e) {
                        logger.error(e.getMessage(), e);
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        removeInventories.addAll(newAddInventory);

        existingDeliveryVoidItems.forEach(
                existDeliveryItem -> {
                    Optional<? extends DeliveryItem> matchItemOpt = deliveryVoidItems.stream()
                            .filter(deliveryItem -> deliveryItem.isSameItem(existDeliveryItem))
                            .findFirst();
                    if (!matchItemOpt.isPresent()) {
                        logger.debug("Add back missing old item[" + existDeliveryItem.getItemRefId() + "] and batch No["
                                + existDeliveryItem.getBatchNumber() + "] that UOM["
                                + existDeliveryItem.getUom() + "] and Qty["
                                + existDeliveryItem.getQuantity() + "]");
                        try {
                            if(checkItemNeedUpdateInventory(drugItems, existDeliveryItem)) {
                                addInventories.add(convertToInventory(existDeliveryItem));
                            }
                        } catch (CMSException e) {
                            logger.error(e.getMessage(), e);
                            throw new RuntimeException(e);
                        }
                    }else{
                        if(!(matchItemOpt.get().isSameItem(existDeliveryItem))){
                            logger.debug("Add changed item[" + existDeliveryItem.getItemRefId() + "] and batch No["
                                    + existDeliveryItem.getBatchNumber() + "] that UOM["
                                    + existDeliveryItem.getUom() + "] and Qty["
                                    + existDeliveryItem.getQuantity() + "]");
                            logger.debug("Remove changed item[" + matchItemOpt.get().getItemRefId() + "] and batch No["
                                    + matchItemOpt.get().getBatchNumber() + "] that UOM["
                                    + matchItemOpt.get().getUom() + "] and Qty["
                                    + matchItemOpt.get().getQuantity() + "]");
                            try {
                                if(checkItemNeedUpdateInventory(drugItems, existDeliveryItem)) {
                                    addInventories.add(convertToInventory(existDeliveryItem));
                                }
                                if(checkItemNeedUpdateInventory(drugItems, existDeliveryItem)) {
                                    removeInventories.add(convertToInventory(matchItemOpt.get()));
                                }
                            } catch (CMSException e) {
                                logger.error(e.getMessage(), e);
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
        );

    }
}
