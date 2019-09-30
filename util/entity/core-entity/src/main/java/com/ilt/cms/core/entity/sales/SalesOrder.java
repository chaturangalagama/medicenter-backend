package com.ilt.cms.core.entity.sales;

import com.ilt.cms.core.entity.PersistedObject;

import java.util.ArrayList;
import java.util.List;

public class SalesOrder extends PersistedObject {

    public enum SalesStatus {
        OPEN, CLOSED
    }

    private int taxValue;
    private String salesNumber;
    private List<SalesItem> purchaseItems;
    private List<Invoice> invoices;
    private List<Dispense> dispenses;
    private SalesStatus status;


    public static SalesOrder newSalesOrder(int taxValue, String salesNumber) {
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.salesNumber = salesNumber;
        salesOrder.taxValue = taxValue;
        salesOrder.purchaseItems = new ArrayList<>();
        salesOrder.invoices = new ArrayList<>();
        salesOrder.dispenses = new ArrayList<>();
        salesOrder.status = SalesStatus.OPEN;
        return salesOrder;
    }

    public void updatePurchaseItems(List<SalesItem> salesItems) {
        purchaseItems = salesItems;
    }

    public int getTaxValue() {
        return taxValue;
    }

    public String getSalesNumber() {
        return salesNumber;
    }

    public List<SalesItem> getPurchaseItems() {
        if (purchaseItems == null) purchaseItems = new ArrayList<>();
        return purchaseItems;
    }

    public List<Invoice> getInvoices() {
        if (invoices == null) invoices = new ArrayList<>();
        return invoices;
    }

    public List<Dispense> getDispenses() {
        if (dispenses == null) dispenses = new ArrayList<>();
        return dispenses;
    }

    public SalesStatus getStatus() {
        return status;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    public void setStatus(SalesStatus status) {
        this.status = status;
    }

    public void setPurchaseItems(List<SalesItem> purchaseItems) {
        if (purchaseItems == null) purchaseItems = new ArrayList<>();
        this.purchaseItems = purchaseItems;
    }

    @Override
    public String toString() {
        return "SalesOrder{" +
                "taxValue=" + taxValue +
                ", salesNumber='" + salesNumber + '\'' +
                ", purchaseItems=" + purchaseItems +
                ", invoices=" + invoices +
                ", dispenses=" + dispenses +
                ", status=" + status +
                '}';
    }
}
