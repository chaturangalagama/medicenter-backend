package com.ilt.cms.inventory.model.purchase.api;

import com.ilt.cms.core.entity.item.SellingPrice;
import com.ilt.cms.inventory.model.purchase.*;

import java.time.LocalDateTime;

public class OrderRequest implements Comparable<OrderRequest>{

    @Override
    public int compareTo(OrderRequest o) {
        if(o.receivedDateTime == null && receivedDateTime == null){
            return 0;
        }else if(o.receivedDateTime == null){
            return 1;
        }else if(receivedDateTime == null){
            return -1;
        }
        return receivedDateTime.compareTo(o.receivedDateTime);
    }

    public enum Filter{
        RECEIVED_DATE_LESS, RECEIVED_DATE_MORE, STATUS, ORDER_REQUEST_NO, TRANSFER_PURCHASE_TYPE, LIST_SIZE
    }

    public OrderRequest(String clinicName, String orderRequestNo, LocalDateTime receivedDateTime, SellingPrice totalPrice,
                        String status, PurchaseOrder purchaseorder, PurchaseRequest purchaserequest, TransferOrder transferOrder,
                        TransferRequest transferRequest) {
        this.clinicName = clinicName;
        this.orderRequestNo = orderRequestNo;
        this.receivedDateTime = receivedDateTime;
        this.totalPrice = totalPrice;
        this.status = status;
        this.purchaseorder = purchaseorder;
        this.purchaserequest = purchaserequest;
        this.transferOrder = transferOrder;
        this.transferRequest = transferRequest;
    }

    private String clinicName;

    private String orderRequestNo;

    private LocalDateTime receivedDateTime;

    private SellingPrice totalPrice;

    private String status;

    private PurchaseOrder purchaseorder;

    private PurchaseRequest purchaserequest;

    private TransferOrder transferOrder;

    private TransferRequest transferRequest;

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getOrderRequestNo() {
        return orderRequestNo;
    }

    public void setOrderResquestNo(String orderRequestNo) {
        this.orderRequestNo = orderRequestNo;
    }

    public LocalDateTime getReceivedDateTime() {
        return receivedDateTime;
    }

    public void setReceivedDateTime(LocalDateTime receivedDateTime) {
        this.receivedDateTime = receivedDateTime;
    }

    public SellingPrice getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(SellingPrice totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOrderRequestNo(String orderRequestNo) {
        this.orderRequestNo = orderRequestNo;
    }

    public PurchaseOrder getPurchaseorder() {
        return purchaseorder;
    }

    public void setPurchaseorder(PurchaseOrder purchaseorder) {
        this.purchaseorder = purchaseorder;
    }

    public PurchaseRequest getPurchaserequest() {
        return purchaserequest;
    }

    public void setPurchaserequest(PurchaseRequest purchaserequest) {
        this.purchaserequest = purchaserequest;
    }

    public TransferOrder getTransferOrder() {
        return transferOrder;
    }

    public void setTransferOrder(TransferOrder transferOrder) {
        this.transferOrder = transferOrder;
    }

    public TransferRequest getTransferRequest() {
        return transferRequest;
    }

    public void setTransferRequest(TransferRequest transferRequest) {
        this.transferRequest = transferRequest;
    }
}
