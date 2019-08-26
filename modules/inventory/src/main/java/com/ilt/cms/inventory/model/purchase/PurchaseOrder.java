package com.ilt.cms.inventory.model.purchase;

import com.ilt.cms.inventory.model.purchase.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.lippo.commons.util.CommonUtils.isStringValid;

public class PurchaseOrder extends Order {

    private String supplierId;

    private List<GoodOrderedItem> goodPurchasedItems;

    private List<GoodReceiveNote> goodReceiveNotes;

    private List<GoodReceiveVoidNote> goodReceiveVoidNotes;


    @Override
    public boolean checkValidate() {
        return isStringValid(supplierId, requestId, requestClinicId) && getOrderStatus() != null && goodPurchasedItems != null;
    }

    public void addGoodPurchasedItem(GoodOrderedItem goodOrderedItem){
        goodPurchasedItems.add(goodOrderedItem);
    }

    public void addGoodReceiveNote(GoodReceiveNote goodReceiveNote){
        goodReceiveNotes.add(goodReceiveNote);
    }

    public void addGoodReceiveVoidNote(GoodReceiveVoidNote goodReceiveVoidNote){
        goodReceiveVoidNotes.add(goodReceiveVoidNote);
    }

    public PurchaseOrder() {
        goodPurchasedItems = new ArrayList<>();
        goodReceiveNotes = new ArrayList<>();
        goodReceiveVoidNotes = new ArrayList<>();
    }

    public PurchaseOrder(String requestId, String requestClinicId, String orderNo, LocalDateTime orderTime, LocalDateTime completeTime,
                         String supplierId, OrderStatus orderStatus) {
        super(requestId, requestClinicId, orderNo, orderTime, completeTime, orderStatus);
        this.supplierId = supplierId;
        this.orderStatus = orderStatus;
        goodPurchasedItems = new ArrayList<>();
        goodReceiveNotes = new ArrayList<>();
        goodReceiveVoidNotes = new ArrayList<>();
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public List<GoodOrderedItem> getGoodPurchasedItems() {
        return goodPurchasedItems;
    }

    public void setGoodPurchasedItems(List<GoodOrderedItem> goodPurchasedItems) {
        this.goodPurchasedItems = goodPurchasedItems;
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

    @Override
    public String toString() {
        return "PurchaseOrder{" +
                "supplierId='" + supplierId + '\'' +
                ", goodPurchasedItems=" + goodPurchasedItems +
                ", goodReceiveNotes=" + goodReceiveNotes +
                ", goodReceiveVoidNotes=" + goodReceiveVoidNotes +
                ", requestId='" + requestId + '\'' +
                ", requestClinicId='" + requestClinicId + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", orderTime=" + orderTime +
                ", completeTime=" + completeTime +
                ", orderStatus=" + orderStatus +
                ", id='" + id + '\'' +
                '}';
    }
}
