package com.ilt.cms.inventory.service.purchase;

import com.ilt.cms.core.entity.item.DrugItem;
import com.ilt.cms.database.RunningNumberService;
import com.ilt.cms.inventory.db.service.interfaces.OrderDatabaseService;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransferService {
    private static final Logger logger = LoggerFactory.getLogger(TransferService.class);

    private OrderDatabaseService orderDatabaseService;
    private RunningNumberService runningNumberService;
    private LocationService locationService;
    private UOMMatrixService uomMatrixService;
    private DrugItemService drugItemService;

    public TransferService(OrderDatabaseService orderDatabaseService, RunningNumberService runningNumberService,
                           LocationService locationService, UOMMatrixService uomMatrixService, DrugItemService drugItemService){
        this.orderDatabaseService = orderDatabaseService;
        this.runningNumberService = runningNumberService;
        this.locationService = locationService;
        this.uomMatrixService = uomMatrixService;
        this.drugItemService = drugItemService;
    }

    public List<TransferRequest> listTransferRequest(){
        List<Request> requests = orderDatabaseService.findTransferRequest();
        return requests.stream()
                .map(request ->(TransferRequest) request)
                .collect(Collectors.toList());
    }

    public List<TransferOrder> listTransferOrder(){
        List<Order> orders = orderDatabaseService.findTransferOrder();
        return orders.stream()
                .map(order -> (TransferOrder) order)
                .collect(Collectors.toList());
    }

    public List<TransferRequest> listTransferRequestByClinicId(String clinicId){
        List<Request> requests = orderDatabaseService.findTransferRequestByClinicId(clinicId);
        return requests.stream()
                .map(request -> (TransferRequest)request)
                .collect(Collectors.toList());
    }

    public List<TransferOrder> listTransferOrderByClinicId(String clinicId){
        List<Order> orders = orderDatabaseService.findTransferOrderByClinicId(clinicId);
        return orders.stream()
                .map(order -> (TransferOrder) order)
                .collect(Collectors.toList());
    }

    public TransferRequest createTransferRequest(TransferRequest transferRequest) throws CMSException {
        if(!transferRequest.checkValidate()){
            throw new CMSException(StatusCode.E1002);
        }
        if(transferRequest.getRequestNo() == null){
            transferRequest.setRequestNo(runningNumberService.generateRequestNumber());
        }
        if(transferRequest.getRequestStatus() == RequestStatus.REQUESTED){
            List<DrugItem> drugItems = drugItemService.listDrugItemByClinicId(transferRequest.getRequestClinicId());
            boolean allMatch = transferRequest.getRequestItems().stream()
                    .allMatch(requestItem ->  checkDrugItemIdExist(drugItems, requestItem.getItemRefId()));
            if(!allMatch){
                throw new CMSException(StatusCode.E2000, "Drug item not found");
            }
        }
        if(transferRequest.getRequestStatus() != RequestStatus.DRAFT && transferRequest.getRequestTime() == null) {
            transferRequest.setRequestTime(LocalDateTime.now());
        }
        return orderDatabaseService.saveTransferRequest(transferRequest);
    }

    public TransferRequest modifyTransferRequest(String transferRequestId, TransferRequest transferRequest) throws CMSException {
        if(!transferRequest.checkValidate()){
            throw new CMSException(StatusCode.E1002, "Invalid in transfer request");
        }

        Optional<TransferRequest> curTransferRequestOpt = orderDatabaseService.findTransferRequestById(transferRequestId);

        if(!curTransferRequestOpt.isPresent()){
            throw new CMSException(StatusCode.E2000, "Transfer request not found");
        }
        TransferRequest curTransferRequest = curTransferRequestOpt.get();
        if(curTransferRequest.getRequestStatus() !=  RequestStatus.DRAFT){
            throw new CMSException(StatusCode.E1010, "Modify transfer request not allow:" + curTransferRequest.getRequestStatus());
        }
        if(transferRequest.getRequestStatus() == RequestStatus.REQUESTED){
            List<DrugItem> drugItems = drugItemService.listDrugItemByClinicId(transferRequest.getRequestClinicId());
            boolean allMatch = transferRequest.getRequestItems().stream()
                    .allMatch(requestItem ->  checkDrugItemIdExist(drugItems, requestItem.getItemRefId()));
            if(!allMatch){
                throw new CMSException(StatusCode.E2000, "Drug item not found");
            }
        }
        if(transferRequest.getRequestNo() == null){
            transferRequest.setRequestNo(runningNumberService.generateRequestNumber());
        }
        if(transferRequest.getRequestStatus() != RequestStatus.DRAFT) {
            curTransferRequest.setRequestTime(LocalDateTime.now());
        }
        curTransferRequest.setRequestStatus(transferRequest.getRequestStatus());
        curTransferRequest.setRequestItems(transferRequest.getRequestItems());
        curTransferRequest.setTransferNote(transferRequest.getTransferNote());

        return orderDatabaseService.saveTransferRequest(curTransferRequest);
    }

    public boolean approveTransferRequest(String transferRequestId) throws CMSException {
        Optional<TransferRequest> curTransferRequestOpt = orderDatabaseService.findTransferRequestById(transferRequestId);

        if(!curTransferRequestOpt.isPresent()){
            throw new CMSException(StatusCode.E2000, "Transfer request not found");
        }

        TransferRequest curTransferRequest = curTransferRequestOpt.get();

        if(curTransferRequest.getRequestStatus() !=  RequestStatus.REQUESTED){
            throw new CMSException(StatusCode.E1010, "Modify transfer request not allow:" + curTransferRequest.getRequestStatus());
        }
        if(!curTransferRequest.checkValidate()){
            throw new CMSException(StatusCode.E1002, "Invalid in transfer request");
        }
        curTransferRequest.setRequestStatus(RequestStatus.APPROVED);
        createTransferOrderByRequestId(transferRequestId);
        orderDatabaseService.saveTransferRequest(curTransferRequest);
        return true;
    }

    public boolean rejectTransferRequest(String transferRequestId) throws CMSException {
        Optional<TransferRequest> curTransferRequestOpt = orderDatabaseService.findTransferRequestById(transferRequestId);

        if(!curTransferRequestOpt.isPresent()){
            throw new CMSException(StatusCode.E2000, "Transfer request not found");
        }

        TransferRequest curTransferRequest = curTransferRequestOpt.get();
        if(curTransferRequest.getRequestStatus() !=  RequestStatus.REQUESTED){
            throw new CMSException(StatusCode.E1010, "Modify transfer request not allow:" + curTransferRequest.getRequestStatus());
        }
        curTransferRequest.setRequestStatus(RequestStatus.REJECTED);
        orderDatabaseService.saveTransferRequest(curTransferRequest);
        return true;

    }

    public TransferOrder createTransferOrderByRequestId(String requestId) throws CMSException {
        Optional<TransferRequest> transferRequestOpt = orderDatabaseService.findTransferRequestById(requestId);
        if(!transferRequestOpt.isPresent()){
            throw new CMSException(StatusCode.E2000, "Transfer request["+requestId+"] not found");
        }
        TransferRequest transferRequest = transferRequestOpt.get();
        if(transferRequest.getRequestStatus() != RequestStatus.REQUESTED){
            throw new CMSException(StatusCode.E1010, "Invalid request status");
        }
        return createTransferOrderByTransferRequest(transferRequestOpt.get());
    }

    public TransferOrder createTransferOrder(TransferOrder transferOrder) throws CMSException {
        if(!transferOrder.checkValidate()){
            throw new CMSException(StatusCode.E1002, "Invalid in transfer order");
        }
        if(!transferOrder.getTransferRequestItems().stream().allMatch(TransferRequestItem::checkValidate)){
            throw new CMSException(StatusCode.E1002, "Invalid in transfer request items");
        }

        List<DrugItem> senderDrugItems = drugItemService.listDrugItemByClinicId(transferOrder.getSenderClinicId());
        if(!transferOrder.getTransferRequestItems().stream()
                .allMatch(transferRequestItem -> checkHaveEnoughStockLevel(transferOrder.getSenderClinicId(),
                        senderDrugItems, transferRequestItem.getItemRefId(),
                        transferRequestItem.getUom(), transferRequestItem.getQuantity()))){
            throw new CMSException(StatusCode.E1010, "Clinic["+transferOrder.getSenderClinicId()+"] Not enough item stock level to transfer");
        }
        if(!transferOrder.getGoodReceiveNotes().stream()
                .flatMap(goodReceiveNote -> goodReceiveNote.getReceivedItems().stream())
                .allMatch(GoodReceivedItem::checkValidate)){
            throw new CMSException(StatusCode.E1002, "Invalid in good received items");
        }
        if(!transferOrder.getGoodReceiveVoidNotes().stream()
                .flatMap(goodReceiveVoidNote -> goodReceiveVoidNote.getReturnItems().stream())
                .allMatch(GoodReturnItem::checkValidate)){
            throw new CMSException(StatusCode.E1002, "Invalid in good return items");
        }
        if(!transferOrder.getDeliveryNotes().stream()
                .flatMap(deliveryNote -> deliveryNote.getTransferSendItems().stream())
                .allMatch(TransferSendItem::checkValidate)){
            throw new CMSException(StatusCode.E1002, "Invalid in transfer send items");
        }

        transferOrder.setOrderNo(runningNumberService.generateOrderNumber());
        logger.info("save transfer order");
        return orderDatabaseService.saveTransferOrder(transferOrder);

    }

    public GoodReceiveNote addGoodReceiveNote(String transferOrderId, GoodReceiveNote goodReceiveNote) throws CMSException {
        if(!goodReceiveNote.checkValidate()){
            throw new CMSException(StatusCode.E1002, "Invalid in good receive note");
        }

        if(!goodReceiveNote.getReceivedItems().stream()
                .allMatch(goodReceivedItem -> goodReceivedItem.checkValidate())){
            throw new CMSException(StatusCode.E1002, "Invalid in good receive Item");
        }

        Optional<TransferOrder> curTransferOrderOpt = orderDatabaseService.findTransferOrderById(transferOrderId);

        if(!curTransferOrderOpt.isPresent()){
            throw new CMSException(StatusCode.E2000, "Order not found");
        }

        TransferOrder curTransferOrder = curTransferOrderOpt.get();

        if(curTransferOrder.getOrderStatus() != OrderStatus.APPROVED){
            throw new CMSException(StatusCode.E1010, "Invalid order status:" + curTransferOrder.getOrderStatus());
        }

        if(goodReceiveNote.getGrnNo() == null){
            goodReceiveNote.setGrnNo(runningNumberService.generateGRNNumber());
        }
        String grnNo = goodReceiveNote.getGrnNo();

        goodReceiveNote.setCreateDate(LocalDate.now());

        curTransferOrder.addGoodReceiveNote(goodReceiveNote);
        //prepare rollback data
        Location backupLocation = locationService.findLocationByClinicId(curTransferOrder.getRequestClinicId());
        Optional<PurchaseOrder> backupPurchaseOrderOpt = orderDatabaseService.findPurchaseOrderById(transferOrderId);

        if (checkIsAllItemReceived(curTransferOrder)) {
            if (curTransferOrder.getOrderStatus() != OrderStatus.REJECTED) {
                curTransferOrder.setOrderStatus(OrderStatus.COMPLETED);
                if (curTransferOrder.getCompleteTime() == null) {
                    curTransferOrder.setCompleteTime(LocalDateTime.now());
                }
            }
        }
        try{
            logger.info("update inventory");
            curTransferOrder = updateRequestClinicStockInventory(curTransferOrder);

            logger.info("save purchase order");
            TransferOrder saveTransferOrder = orderDatabaseService.saveTransferOrder(curTransferOrder);
            Optional<GoodReceiveNote> saveGoodReceiveNoteOpt = saveTransferOrder.getGoodReceiveNotes().stream()
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

    public GoodReceiveVoidNote addGoodReceiveVoidNote(String transferOrderId, GoodReceiveVoidNote goodReceiveVoidNote) throws CMSException {
        if(!goodReceiveVoidNote.checkValidate()){
            throw new CMSException(StatusCode.E1002, "Invalid in good receive void note");
        }

        if(!goodReceiveVoidNote.getReturnItems().stream()
                .allMatch(returnItem -> returnItem.checkValidate())){
            throw new CMSException(StatusCode.E1002, "Invalid in good return Item");
        }

        Optional<TransferOrder> curTransferOrderOpt = orderDatabaseService.findTransferOrderById(transferOrderId);

        if(!curTransferOrderOpt.isPresent()){
            throw new CMSException(StatusCode.E2000, "Order not found");
        }

        TransferOrder curTransferOrder = curTransferOrderOpt.get();

        if(curTransferOrder.getOrderStatus() != OrderStatus.APPROVED){
            throw new CMSException(StatusCode.E1010, "Invalid order status:" + curTransferOrder.getOrderStatus());
        }

        List<GoodReceivedItem> curGoodReceivedItems = curTransferOrder.getGoodReceiveNotes().stream()
                .flatMap(goodReceiveNote -> goodReceiveNote.getReceivedItems().stream())
                .collect(Collectors.toList());
        System.out.println(curGoodReceivedItems);
        goodReceiveVoidNote.getReturnItems().forEach(
                returnItem -> {
                    System.out.println(returnItem);
                    Optional<GoodReceivedItem> receivedItemOpt = curGoodReceivedItems.stream()
                            .filter(goodReceivedItem -> goodReceivedItem.getItemRefId().equals(returnItem.getItemRefId()) &&
                                    goodReceivedItem.getBatchNumber().equals(returnItem.getBatchNumber())).findFirst();
                    if(!receivedItemOpt.isPresent()){
                        throw new RuntimeException(new CMSException(StatusCode.E2000, "Good receive item not found"));
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

        curTransferOrder.addGoodReceiveVoidNote(goodReceiveVoidNote);
        //prepare rollback data
        Location backupLocation = locationService.findLocationByClinicId(curTransferOrder.getRequestClinicId());
        Optional<TransferOrder> backupTransferOrderOpt = orderDatabaseService.findTransferOrderById(transferOrderId);

        try{
            logger.info("update inventory");
            curTransferOrder = updateRequestClinicStockInventory(curTransferOrder);

            logger.info("save purchase order");
            TransferOrder saveTransferOrder = orderDatabaseService.saveTransferOrder(curTransferOrder);
            Optional<GoodReceiveVoidNote> saveGoodReceiveVoidNoteOpt = saveTransferOrder.getGoodReceiveVoidNotes().stream()
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
            orderDatabaseService.saveTransferOrder(backupTransferOrderOpt.get());
            logger.info("rollback PurchaseOrder completed");
            throw e;

        }
    }

    public DeliveryNote addDeliveryNote(String transferOrderId, DeliveryNote deliveryNote) throws CMSException {
        if(!deliveryNote.checkValidate()){
            throw new CMSException(StatusCode.E1002, "Invalid in delivery note");
        }

        if(!deliveryNote.getTransferSendItems().stream()
                .allMatch(transferSendItem -> transferSendItem.checkValidate())){
            throw new CMSException(StatusCode.E1002, "Invalid in transfer send Item");
        }

        Optional<TransferOrder> curTransferOrderOpt = orderDatabaseService.findTransferOrderById(transferOrderId);

        if(!curTransferOrderOpt.isPresent()){
            throw new CMSException(StatusCode.E2000, "Order not found");
        }

        TransferOrder curTransferOrder = curTransferOrderOpt.get();

        if(curTransferOrder.getOrderStatus() != OrderStatus.APPROVED){
            throw new CMSException(StatusCode.E1010, "Invalid order status:" + curTransferOrder.getOrderStatus());
        }

        if(deliveryNote.getDeliveryNoteNo() == null){
            deliveryNote.setDeliveryNoteNo(runningNumberService.generateDeliveryNote());
        }
        String deliveryNoteGrnNo = deliveryNote.getDeliveryNoteNo();

        deliveryNote.setCreateDate(LocalDate.now());

        curTransferOrder.addDeliveryNote(deliveryNote);
        //prepare rollback data
        Location backupLocation = locationService.findLocationByClinicId(curTransferOrder.getRequestClinicId());
        Optional<TransferOrder> backupTransferOrderOpt = orderDatabaseService.findTransferOrderById(transferOrderId);

        try{
            logger.info("update inventory");
            curTransferOrder = updateSenderClinicStockInventory(curTransferOrder);

            logger.info("save purchase order");
            TransferOrder saveTransferOrder = orderDatabaseService.saveTransferOrder(curTransferOrder);
            Optional<DeliveryNote> saveDeliveryNoteOpt = saveTransferOrder.getDeliveryNotes().stream()
                    .filter(deliveryNote1 -> deliveryNote1.getDeliveryNoteNo().equals(deliveryNoteGrnNo))
                    .findFirst();
            if(saveDeliveryNoteOpt.isPresent()){
                return saveDeliveryNoteOpt.get();
            }
            return null;
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            logger.info("rollback data");
            if(backupLocation != null) {
                locationService.saveLocation(backupLocation);
                logger.info("rollback location completed");
            }
            orderDatabaseService.saveTransferOrder(backupTransferOrderOpt.get());
            logger.info("rollback PurchaseOrder completed");
            throw e;

        }
    }

    public DeliveryVoidNote addDeliveryVoidNote(String transferOrderId, DeliveryVoidNote deliveryVoidNote) throws CMSException {
        if(!deliveryVoidNote.checkValidate()){
            throw new CMSException(StatusCode.E1002, "Invalid in good receive note");
        }

        if(!deliveryVoidNote.getTransferSendVoidItems().stream()
                .allMatch(transferSendVoidItem -> transferSendVoidItem.checkValidate())){
            throw new CMSException(StatusCode.E1002, "Invalid in good receive Item");
        }

        Optional<TransferOrder> curTransferOrderOpt = orderDatabaseService.findTransferOrderById(transferOrderId);

        if(!curTransferOrderOpt.isPresent()){
            throw new CMSException(StatusCode.E2000, "Order not found");
        }

        TransferOrder curTransferOrder = curTransferOrderOpt.get();

        if(curTransferOrder.getOrderStatus() != OrderStatus.APPROVED){
            throw new CMSException(StatusCode.E1010, "Invalid order status:" + curTransferOrder.getOrderStatus());
        }
        if(deliveryVoidNote.getDvnNo() == null){
            deliveryVoidNote.setDvnNo(runningNumberService.generateDeliveryVoidNote());
        }

        String dvnNo = deliveryVoidNote.getDvnNo();

        deliveryVoidNote.setCreateDate(LocalDate.now());

        curTransferOrder.addDeliveryVoidNote(deliveryVoidNote);
        //prepare rollback data
        Location backupLocation = locationService.findLocationByClinicId(curTransferOrder.getRequestClinicId());
        Optional<TransferOrder> backupTransferOrderOpt = orderDatabaseService.findTransferOrderById(transferOrderId);

        try{
            logger.info("update inventory");
            curTransferOrder = updateSenderClinicStockInventory(curTransferOrder);

            logger.info("save purchase order");
            TransferOrder saveTransferOrder = orderDatabaseService.saveTransferOrder(curTransferOrder);
            Optional<DeliveryVoidNote> saveDeliveryVoidNoteOpt = saveTransferOrder.getDeliveryVoidNotes().stream()
                    .filter(deliveryNote1 -> deliveryNote1.getDvnNo().equals(dvnNo))
                    .findFirst();
            if(saveDeliveryVoidNoteOpt.isPresent()){
                return saveDeliveryVoidNoteOpt.get();
            }
            return null;
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            logger.info("rollback data");
            if(backupLocation != null) {
                locationService.saveLocation(backupLocation);
                logger.info("rollback location completed");
            }
            orderDatabaseService.saveTransferOrder(backupTransferOrderOpt.get());
            logger.info("rollback PurchaseOrder completed");
            throw e;

        }
    }

    private boolean checkIsAllItemReceived(TransferOrder transferOrder){
        List<GoodReceivedItem> goodReceivedItems = transferOrder.getGoodReceiveNotes().stream()
                .flatMap(goodReceiveNote -> goodReceiveNote.getReceivedItems().stream())
                .collect(Collectors.toList());

        List<GoodReturnItem> goodReturnItems = transferOrder.getGoodReceiveVoidNotes().stream()
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

        boolean isAllReceived = transferOrder.getTransferRequestItems().stream().allMatch(transferRequestItem ->{
            UOM baseUom = null;
            Double receivedQty = itemIdQtyMap.get(transferRequestItem.getItemRefId());
            if(receivedQty == null){
                logger.info("no received quantity for item id:" + transferRequestItem.getItemRefId());
                receivedQty = 0.0;
            }
            try {
                baseUom = locationService.findBaseUomByItemId(transferRequestItem.getItemRefId());
            } catch (CMSException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
            Double ratio = null;
            try {
                ratio = uomMatrixService.findRatio(transferRequestItem.getUom(), baseUom.getCode());
                logger.info("UOM ratio["+ratio+"]");
            } catch (CMSException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
            logger.info("Compare received item ["+transferRequestItem.getItemRefId()+"] received qty["+receivedQty+
                    "], ordered qty["+(ratio * transferRequestItem.getQuantity())+"]");
            if(ratio * transferRequestItem.getQuantity() <= receivedQty){
                return true;
            }
            return false;
        });

        return isAllReceived;
    }

    private TransferOrder updateSenderClinicStockInventory(TransferOrder transferOrder) throws CMSException{
        logger.info("updateSenderClinicStockInventory");
        //init array
        List<Inventory> addInventory = new ArrayList<>();
        List<Inventory> removeInventory = new ArrayList<>();
        List<TransferSendItem> existTransferSendItems = null;
        List<TransferSendVoidItem> existTransferSendVoidItems = null;
        List<DrugItem> drugItems = drugItemService.listDrugItemByClinicId(transferOrder.getSenderClinicId());
        //check get exist transfer order
        Optional<TransferOrder> curTransferOrderOpt;
        if(transferOrder.getId() != null) {
            curTransferOrderOpt = orderDatabaseService.findTransferOrderById(transferOrder.getId());
            if(curTransferOrderOpt.isPresent()){
                existTransferSendItems = curTransferOrderOpt.get().getDeliveryNotes().stream()
                        .flatMap(deliveryNote -> deliveryNote.getTransferSendItems().stream())
                        .collect(Collectors.toList());
                existTransferSendVoidItems = curTransferOrderOpt.get().getDeliveryVoidNotes().stream()
                        .flatMap(deliveryVoidNote -> deliveryVoidNote.getTransferSendVoidItems().stream())
                        .collect(Collectors.toList());
            }
        }
        List<TransferSendItem> transferSendItems = transferOrder.getDeliveryNotes().stream()
                .flatMap(deliveryNote -> deliveryNote.getTransferSendItems().stream())
                .collect(Collectors.toList());

        addDeliveryVoidItemToInventoryList(transferSendItems, existTransferSendItems, addInventory,
                removeInventory, drugItems);

        List<TransferSendVoidItem> transferSendVoidItems = transferOrder.getDeliveryVoidNotes().stream()
                .flatMap(deliveryVoidNote -> deliveryVoidNote.getTransferSendVoidItems().stream())
                .collect(Collectors.toList());
        addDeliveryItemToInventoryList(transferSendVoidItems, existTransferSendVoidItems, addInventory,
                removeInventory, drugItems);


        adjustInventory(transferOrder.getSenderClinicId(), addInventory, removeInventory);

        logger.info("set flag for counted item");
        transferOrder.getDeliveryNotes()
            .forEach(deliveryNote -> {
                deliveryNote.getTransferSendItems()
                    .forEach(
                        transferSendItem -> {
                            if(transferSendItem.getId() == null){
                                transferSendItem.setId(CommonUtils.idGenerator());
                            }
                            if(checkItemNeedUpdateInventory(drugItems, transferSendItem)) {
                                transferSendItem.setCountInStock(true);
                            }
                        }
                    );
            });

        transferOrder.getDeliveryVoidNotes()
            .forEach(deliveryVoidNote -> {
                deliveryVoidNote.getTransferSendVoidItems()
                    .forEach(
                        transferSendVoidItem -> {
                            if(transferSendVoidItem.getId() == null){
                                transferSendVoidItem.setId(CommonUtils.idGenerator());
                            }
                            if(checkItemNeedUpdateInventory(drugItems, transferSendVoidItem)) {
                                transferSendVoidItem.setCountInStock(true);
                            }
                        }
                    );
            });

        return transferOrder;
    }

    private TransferOrder updateRequestClinicStockInventory(TransferOrder transferOrder) throws CMSException {
        logger.info("updateRequestClinicStockInventory");
        //init array
        List<Inventory> addInventory = new ArrayList<>();
        List<Inventory> removeInventory = new ArrayList<>();
        List<GoodReceivedItem> existGoodReceivedItems = new ArrayList<>();
        List<GoodReturnItem> existGoodReturnItems = new ArrayList<>();
        List<DrugItem> drugItems = drugItemService.listDrugItemByClinicId(transferOrder.getRequestClinicId());
        //check get exist transferOrder
        Optional<TransferOrder> curTransferOrderOpt;
        if(transferOrder.getId() != null) {
            curTransferOrderOpt = orderDatabaseService.findTransferOrderById(transferOrder.getId());
            if(curTransferOrderOpt.isPresent()){
                existGoodReceivedItems = curTransferOrderOpt.get().getGoodReceiveNotes().stream()
                        .flatMap(goodReceiveNote -> goodReceiveNote.getReceivedItems().stream())
                        .collect(Collectors.toList());
                existGoodReturnItems = curTransferOrderOpt.get().getGoodReceiveVoidNotes().stream()
                        .flatMap(goodReceiveVoidNote -> goodReceiveVoidNote.getReturnItems().stream())
                        .collect(Collectors.toList());
            }
        }
        List<GoodReceivedItem> goodReceivedItems = transferOrder.getGoodReceiveNotes().stream()
                .flatMap(goodReceiveNote -> goodReceiveNote.getReceivedItems().stream())
                .collect(Collectors.toList());

        addDeliveryItemToInventoryList(goodReceivedItems, existGoodReceivedItems,
                addInventory, removeInventory, drugItems);

        List<GoodReturnItem> goodReturnItems = transferOrder.getGoodReceiveVoidNotes().stream()
                .flatMap(goodReceiveVoidNote -> goodReceiveVoidNote.getReturnItems().stream())
                .collect(Collectors.toList());
        addDeliveryVoidItemToInventoryList(goodReturnItems, existGoodReturnItems,
                addInventory, removeInventory, drugItems);

        /*if(curTransferOrderOpt.isPresent()){
            List<GoodReceivedItem> countedStockItems = transferOrder.getGoodReceiveNotes().stream()
                    .flatMap(goodReceiveNote -> goodReceiveNote.getReceivedItems().stream())
                    .filter(goodReceivedItem -> goodReceivedItem.getId() != null)
                    .collect(Collectors.toList());

            List<GoodReceivedItem> notCountedStockItems = transferOrder.getGoodReceiveNotes().stream()
                    .flatMap(goodReceiveNote -> goodReceiveNote.getReceivedItems().stream())
                    .filter(goodReceivedItem -> goodReceivedItem.getId() == null)
                    .collect(Collectors.toList());

            notCountedStockItems.forEach(
                    goodReceivedItem -> {
                        try {
                            logger.debug("Add new item[" + goodReceivedItem.getItemRefId() + "] and batch No["
                                    + goodReceivedItem.getBatchNumber() + "] that UOM["
                                    + goodReceivedItem.getUom() + "] and Qty["
                                    + goodReceivedItem.getQuantity() + "]");
                            if(checkItemNeedUpdateInventory(drugItems, goodReceivedItem)) {
                                addInventory.add(convertToInventory(goodReceivedItem));
                            }
                        } catch (CMSException e) {
                            logger.error(e.getMessage(), e);
                            throw new RuntimeException(e);
                        }
                    }
            );

            existGoodReceivedItems.forEach(
                    existGoodReceivedItem -> {
                        Optional<GoodReceivedItem> matchItemOpt = countedStockItems.stream()
                                .filter(goodReceivedItem -> goodReceivedItem.getId().equals(existGoodReceivedItem.getId()))
                                .findFirst();
                        if (!matchItemOpt.isPresent()) {
                            logger.debug("Remove missing old item[" + existGoodReceivedItem.getItemRefId() + "] and batch No["
                                    + existGoodReceivedItem.getBatchNumber() + "] that UOM["
                                    + existGoodReceivedItem.getUom() + "] and Qty["
                                    + existGoodReceivedItem.getQuantity() + "]");
                            try {
                                if(checkItemNeedUpdateInventory(drugItems, existGoodReceivedItem)) {
                                    removeInventory.add(convertToInventory(existGoodReceivedItem));
                                }
                            } catch (CMSException e) {
                                logger.error(e.getMessage(), e);
                                throw new RuntimeException(e);
                            }
                        }else{
                            if(!(matchItemOpt.get().getUom().equals(existGoodReceivedItem.getUom()) &&
                                    matchItemOpt.get().getQuantity() == existGoodReceivedItem.getQuantity())){
                                logger.debug("Remove changed item[" + existGoodReceivedItem.getItemRefId() + "] and batch No["
                                        + existGoodReceivedItem.getBatchNumber() + "] that UOM["
                                        + existGoodReceivedItem.getUom() + "] and Qty["
                                        + existGoodReceivedItem.getQuantity() + "]");
                                logger.debug("Add changed item[" + matchItemOpt.get().getItemRefId() + "] and batch No["
                                        + matchItemOpt.get().getBatchNumber() + "] that UOM["
                                        + matchItemOpt.get().getUom() + "] and Qty["
                                        + matchItemOpt.get().getQuantity() + "]");
                                try {
                                    if(checkItemNeedUpdateInventory(drugItems, existGoodReceivedItem)) {
                                        removeInventory.add(convertToInventory(existGoodReceivedItem));
                                    }
                                    if(checkItemNeedUpdateInventory(drugItems, existGoodReceivedItem)) {
                                        addInventory.add(convertToInventory(matchItemOpt.get()));
                                    }
                                } catch (CMSException e) {
                                    logger.error(e.getMessage(), e);
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
            );

            List<GoodReturnItem> countedReturnStockItems = transferOrder.getGoodReceiveVoidNotes().stream()
                    .flatMap(goodReceiveVoidNote -> goodReceiveVoidNote.getReturnItems().stream())
                    .filter(goodReturnItem -> goodReturnItem.getId() != null)
                    .collect(Collectors.toList());

            List<GoodReturnItem> notCountedReturnStockItems = transferOrder.getGoodReceiveVoidNotes().stream()
                    .flatMap(goodReceiveVoidNote -> goodReceiveVoidNote.getReturnItems().stream())
                    .filter(goodReturnItem -> goodReturnItem.getId() == null)
                    .collect(Collectors.toList());

            notCountedReturnStockItems.forEach(
                    goodReturnItem -> {
                        try {
                            logger.debug("Remove new item[" + goodReturnItem.getItemRefId() + "] and batch No["
                                    + goodReturnItem.getBatchNumber() + "] that UOM["
                                    + goodReturnItem.getUom() + "] and Qty["
                                    + goodReturnItem.getQuantity() + "]");
                            if(checkItemNeedUpdateInventory(drugItems, goodReturnItem)) {
                                removeInventory.add(convertToInventory(goodReturnItem));
                            }
                        } catch (CMSException e) {
                            logger.error(e.getMessage(), e);
                            throw new RuntimeException(e);
                        }
                    }
            );

            existGoodReturnItems.forEach(
                    existGoodReturnItem -> {
                        Optional<GoodReturnItem> matchItemOpt = countedReturnStockItems.stream()
                                .filter(goodReturnItem -> goodReturnItem.getId().equals(existGoodReturnItem.getId()))
                                .findFirst();
                        if (!matchItemOpt.isPresent()) {
                            logger.debug("Remove missing old item[" + existGoodReturnItem.getItemRefId() + "] and batch No["
                                    + existGoodReturnItem.getBatchNumber() + "] that UOM["
                                    + existGoodReturnItem.getUom() + "] and Qty["
                                    + existGoodReturnItem.getQuantity() + "]");
                            try {
                                if(checkItemNeedUpdateInventory(drugItems, existGoodReturnItem)) {
                                    addInventory.add(convertToInventory(existGoodReturnItem));
                                }
                            } catch (CMSException e) {
                                logger.error(e.getMessage(), e);
                                throw new RuntimeException(e);
                            }
                        }else{
                            if(!(matchItemOpt.get().getUom().equals(existGoodReturnItem.getUom()) &&
                                    matchItemOpt.get().getQuantity() == existGoodReturnItem.getQuantity())){
                                logger.debug("Add changed item[" + existGoodReturnItem.getItemRefId() + "] and batch No["
                                        + existGoodReturnItem.getBatchNumber() + "] that UOM["
                                        + existGoodReturnItem.getUom() + "] and Qty["
                                        + existGoodReturnItem.getQuantity() + "]");
                                logger.debug("Remove changed item[" + matchItemOpt.get().getItemRefId() + "] and batch No["
                                        + matchItemOpt.get().getBatchNumber() + "] that UOM["
                                        + matchItemOpt.get().getUom() + "] and Qty["
                                        + matchItemOpt.get().getQuantity() + "]");
                                try {
                                    if(checkItemNeedUpdateInventory(drugItems, existGoodReturnItem)) {
                                        addInventory.add(convertToInventory(existGoodReturnItem));
                                    }
                                    if(checkItemNeedUpdateInventory(drugItems, matchItemOpt.get())) {
                                        removeInventory.add(convertToInventory(matchItemOpt.get()));
                                    }
                                } catch (CMSException e) {
                                    logger.error(e.getMessage(), e);
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
            );

        }else{
            List<Inventory> addNewStockItems = transferOrder.getGoodReceiveNotes().stream()
                    .flatMap(goodReceiveNote -> goodReceiveNote.getReceivedItems().stream())
                    .map(goodReceivedItem -> {
                        try {
                            return convertToInventory(goodReceivedItem);
                        } catch (CMSException e) {
                            logger.error(e.getMessage(), e);
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());

            addNewStockItems.forEach(goodReceivedItem -> logger.debug("Add back new item["
                    +goodReceivedItem.getItemRefId()+"] and batch No["
                    +goodReceivedItem.getBatchNumber()+"] that UOM["
                    +goodReceivedItem.getBaseUom()+"] and Qty["
                    +goodReceivedItem.getAvailableCount()+"]"));

            addInventory.addAll(addNewStockItems);

            List<Inventory> removeNewStockItems = transferOrder.getGoodReceiveVoidNotes().stream()
                    .flatMap(goodReceiveVoidNote -> goodReceiveVoidNote.getReturnItems().stream())
                    .map(goodReturnItem -> {
                        try {
                            return convertToInventory(goodReturnItem);
                        } catch (CMSException e) {
                            logger.error(e.getMessage(), e);
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());

            removeNewStockItems.forEach(goodReturnItem -> logger.debug("Remove back new item["
                    +goodReturnItem.getItemRefId()+"] and batch No["
                    +goodReturnItem.getBatchNumber()+"] that UOM["
                    +goodReturnItem.getBaseUom()+"] and Qty["
                    +goodReturnItem.getAvailableCount()+"]"));

            removeInventory.addAll(removeNewStockItems);
        }*/

        adjustInventory(transferOrder.getRequestClinicId(), addInventory, removeInventory);

        logger.info("set flag for counted item");
        transferOrder.getGoodReceiveNotes()
                .forEach(goodReceiveNote -> {
                    goodReceiveNote.getReceivedItems()
                    .forEach(
                        goodReceivedItem -> {
                            if(goodReceivedItem.getId() == null){
                                goodReceivedItem.setId(CommonUtils.idGenerator());
                            }
                            if(checkItemNeedUpdateInventory(drugItems, goodReceivedItem)) {
                                goodReceivedItem.setCountInStock(true);
                            }
                        }
                    );
                });

        transferOrder.getGoodReceiveVoidNotes()
                .forEach(goodReceiveVoidNote -> {
                    goodReceiveVoidNote.getReturnItems()
                        .forEach(
                            goodReturnItem -> {
                                if(goodReturnItem.getId() == null){
                                    goodReturnItem.setId(CommonUtils.idGenerator());
                                }
                                if(checkItemNeedUpdateInventory(drugItems, goodReturnItem)) {
                                    goodReturnItem.setCountInStock(true);
                                }
                            }
                        );
                });

        return transferOrder;
    }

    private int roundingQuantity(double quntity){
        return (int)((Math.round(quntity * 100) / 100D) * 100);
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
                roundingQuantity(ratio * deliveryItem.getQuantity()), false);
        return inventory;
    }

    private Location adjustInventory(String clinicId, List<Inventory> addInventory, List<Inventory> removeInventory) throws CMSException {
        Location location = locationService.findLocationByClinicId(clinicId);
        List<DrugItem> drugItems = drugItemService.listDrugItemByClinicId(clinicId);
        List<Inventory> curInventories = new ArrayList<>();
        curInventories.addAll(location.getInventory());

        addInventory.forEach(inventory -> {
            curInventories.stream()
                    .filter(curInventory -> curInventory.getItemRefId().equals(inventory.getItemRefId())
                            && curInventory.getBatchNumber().equals(inventory.getBatchNumber())
                    ).findFirst();
//                    .ifPresentOrElse(existInventory -> {
//                        existInventory.setAvailableCount(existInventory.getAvailableCount() + inventory.getAvailableCount());
//                        existInventory.setExpiryDate(inventory.getExpiryDate());
//                        logger.debug("Available count update to ["+existInventory.getAvailableCount()+"]");
//                    }, ()-> {
//                            try {
//                                curInventories.add(inventory);
//                                logger.debug("Available count update to [" + inventory.getAvailableCount() + "]");
//                            }catch (Exception e){
//                                logger.error(e.getMessage(), e);
//                            }
//                        }
//                    );
        });
        removeInventory.stream()
                .forEach(inventory -> {
                    curInventories.stream()
                            .filter(curInventory -> curInventory.getItemRefId().equals(inventory.getItemRefId())
                                    && curInventory.getBatchNumber().equals(inventory.getBatchNumber())
                            ).findFirst();
//                            .ifPresentOrElse(existInventory -> {
//                                existInventory.setAvailableCount(existInventory.getAvailableCount() - inventory.getAvailableCount());
//                                existInventory.setExpiryDate(inventory.getExpiryDate());
//                                logger.debug("Available count update to ["+existInventory.getAvailableCount()+"]");
//                            }, ()-> {
//                                    try {
//                                        inventory.setAvailableCount(0 - inventory.getAvailableCount());
//                                        logger.debug("Available count update to ["+inventory.getAvailableCount()+"]");
//                                        curInventories.add(inventory);
//                                    }catch (Exception e){
//                                        logger.error(e.getMessage(), e);
//                                    }
//                                }
//                            );

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


        logger.debug("curInventories:" + curInventories);
        location.setInventory(curInventories);
        if(location.getId() != null) {
            location = locationService.modifyLocation(location.getId(), location);
        }else {
            location = locationService.saveLocation(location);
        }
        return location;
    }

    private TransferOrder createTransferOrderByTransferRequest(TransferRequest transferRequest) throws CMSException {
        TransferOrder transferOrder = new TransferOrder(transferRequest.getId(), transferRequest.getRequestClinicId(),
                null, LocalDateTime.now(), null, transferRequest.getSenderClinicId(), OrderStatus.APPROVED);
        List<TransferRequestItem> transferRequestItems = transferRequest.getRequestItems().stream()
                .map(this::mapRequestItemToTransferRequestItem)
                .collect(Collectors.toList());
        transferOrder.setTransferRequestItems(transferRequestItems);
        return createTransferOrder(transferOrder);
    }

    private TransferRequestItem mapRequestItemToTransferRequestItem(RequestItem requestItem){
        TransferRequestItem goodOrderedItem = new TransferRequestItem(requestItem.getItemRefId(), requestItem.getUom(),
                requestItem.getQuantity());
        return goodOrderedItem;
    }

    private boolean checkDrugItemIdExist(List<DrugItem> drugItems, String itemRefId){
        return drugItems.stream()
                .anyMatch(drugItem -> drugItem.getId().equals(itemRefId));
    }

    private boolean checkHaveEnoughStockLevel(String clinicId, List<DrugItem> drugItems, String itemRefId, String uom, int quantity) {
        Optional<DrugItem> drugItemOpt = drugItems.stream().filter(drugItem -> drugItem.getId().equals(itemRefId)).findFirst();
        if(drugItemOpt.isPresent()) {
            if(!drugItemOpt.get().isInventoried()){
                return true;
            }

            Location location = null;
            try {
                location = locationService.findLocationByClinicId(clinicId);
            } catch (CMSException e) {
                logger.error(e.getMessage(), e);
            }
            if(location == null){
                return false;
            }
            Integer existItemStockLevel = location.getInventory().stream().
                    filter(inventory -> inventory.getItemRefId().equals(itemRefId)
                            && inventory.getExpiryDate() != null ? inventory.getExpiryDate().isAfter(LocalDate.now()) : true)
                    .collect(Collectors.summingInt(Inventory::getAvailableCount));
            double ratio;
            try {
                ratio = uomMatrixService.findRatio(uom, drugItemOpt.get().getBaseUom());
            } catch (CMSException e) {
                logger.error(e.getMessage(), e);
                return false;
            }

            if(ratio * quantity < existItemStockLevel
                    && drugItemOpt.get().getSafetyStockQty() < existItemStockLevel ){
                return true;
            }
        }
        return false;

    }

    private boolean checkItemNeedUpdateInventory(List<DrugItem> drugItems, DeliveryItem deliveryItem){
        Optional<DrugItem> drugItemOpt = drugItems.stream()
                .filter(drugItem -> drugItem.getId().equals(deliveryItem.getItemRefId()))
                .findFirst();
        if(drugItemOpt.isPresent()){
            DrugItem drugItem = drugItemOpt.get();
            logger.debug("Item store in inventory["+drugItem.isInventoried()+"]");
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

        List<Inventory> newAddInventory = deliveryVoidItems.stream()
                .filter(deliveryItem -> deliveryItem.getId() == null && checkItemNeedUpdateInventory(drugItems, deliveryItem))
                .map(deliveryItem -> {
                    try {
                        logger.debug("Drop new item[" + deliveryItem.getItemRefId() + "] and batch No["
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
