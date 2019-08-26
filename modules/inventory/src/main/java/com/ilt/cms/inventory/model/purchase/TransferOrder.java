package com.ilt.cms.inventory.model.purchase;

import com.ilt.cms.inventory.model.purchase.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.lippo.commons.util.CommonUtils.isStringValid;

public class TransferOrder extends Order {

    private String senderClinicId;

    private List<TransferRequestItem> transferRequestItems;

    private List<DeliveryNote> deliveryNotes;

    private List<DeliveryVoidNote> deliveryVoidNotes;

    private List<GoodReceiveNote> goodReceiveNotes;

    private List<GoodReceiveVoidNote> goodReceiveVoidNotes;

    public void addGoodReceiveNote(GoodReceiveNote goodReceiveNote){
        goodReceiveNotes.add(goodReceiveNote);
    }

    public void addGoodReceiveVoidNote(GoodReceiveVoidNote goodReceiveVoidNote){
        goodReceiveVoidNotes.add(goodReceiveVoidNote);
    }
    public void addDeliveryNote(DeliveryNote deliveryNote) {
        deliveryNotes.add(deliveryNote);
    }
    public void addDeliveryVoidNote(DeliveryVoidNote deliveryVoidNote) {
        deliveryVoidNotes.add(deliveryVoidNote);
    }
    @Override
    public boolean checkValidate() {
        return isStringValid(senderClinicId, requestId, requestClinicId) && getOrderStatus() != null && transferRequestItems != null;
    }

    public TransferOrder() {
        this.transferRequestItems = new ArrayList<>();
        this.deliveryNotes = new ArrayList<>();
        this.deliveryVoidNotes = new ArrayList<>();
        this.goodReceiveNotes = new ArrayList<>();
        this.goodReceiveVoidNotes = new ArrayList<>();
    }

    public TransferOrder(String requestId, String requestClinicId, String orderNo, LocalDateTime orderTime, LocalDateTime completeTime,
                         String senderClinicId, OrderStatus orderStatus) {
        super(requestId, requestClinicId, orderNo, orderTime, completeTime, orderStatus);
        this.senderClinicId = senderClinicId;
        this.orderStatus = orderStatus;
        this.transferRequestItems = new ArrayList<>();
        this.deliveryNotes = new ArrayList<>();
        this.deliveryVoidNotes = new ArrayList<>();
        this.goodReceiveNotes = new ArrayList<>();
        this.goodReceiveVoidNotes = new ArrayList<>();
    }

    public String getSenderClinicId() {
        return senderClinicId;
    }

    public void setSenderClinicId(String senderClinicId) {
        this.senderClinicId = senderClinicId;
    }

    public List<TransferRequestItem> getTransferRequestItems() {
        return transferRequestItems;
    }

    public void setTransferRequestItems(List<TransferRequestItem> transferRequestItems) {
        this.transferRequestItems = transferRequestItems;
    }

    public List<DeliveryNote> getDeliveryNotes() {
        return deliveryNotes;
    }

    public void setDeliveryNotes(List<DeliveryNote> deliveryNotes) {
        this.deliveryNotes = deliveryNotes;
    }

    public List<GoodReceiveNote> getGoodReceiveNotes() {
        return goodReceiveNotes;
    }

    public void setGoodReceiveNotes(List<GoodReceiveNote> goodReceiveNotes) {
        this.goodReceiveNotes = goodReceiveNotes;
    }

    public List<GoodReceiveVoidNote> getGoodReceiveVoidNotes() {
        return goodReceiveVoidNotes;
    }

    public void setGoodReceiveVoidNotes(List<GoodReceiveVoidNote> goodReceiveVoidNotes) {
        this.goodReceiveVoidNotes = goodReceiveVoidNotes;
    }

    public List<DeliveryVoidNote> getDeliveryVoidNotes() {
        return deliveryVoidNotes;
    }

    public void setDeliveryVoidNotes(List<DeliveryVoidNote> deliveryVoidNotes) {
        this.deliveryVoidNotes = deliveryVoidNotes;
    }


}
