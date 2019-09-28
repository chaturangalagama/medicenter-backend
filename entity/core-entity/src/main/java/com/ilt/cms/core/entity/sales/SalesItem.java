package com.ilt.cms.core.entity.sales;

import com.ilt.cms.core.entity.item.Cost;
import com.ilt.cms.core.entity.item.Item;
import com.ilt.cms.core.entity.item.SellingPrice;
import com.lippo.cms.util.Calculations;

import java.time.LocalDate;

public class SalesItem {

    private String itemRefId;
    private Cost cost;
    private SellingPrice sellingPrice;
    private int soldPrice;
    private int dosage;
    private int duration;
    private int purchaseQty;
    private String instruct;
    private String batchNo;
    private String purchaseUom;
    private LocalDate expireDate;
    private String remarks;
    private ItemPriceAdjustment itemPriceAdjustment;
    private Item.ItemType itemType;

    public SalesItem() {
    }

    public SalesItem(Item item, SalesItem si) {

        itemRefId = item.getId();
        cost = item.getCost();
        itemType = item.getItemType();

        if (si != null) {
            sellingPrice = si.getSellingPrice();
            dosage = si.getDosage();
            duration = si.getDuration();
            purchaseQty = si.getPurchaseQty();
            instruct = si.getInstruct();
            batchNo = si.getBatchNo();
            purchaseUom = si.getPurchaseUom();
            expireDate = si.getExpireDate();
            remarks = si.getRemarks();
            itemPriceAdjustment = si.getItemPriceAdjustment();

            //calculate the sold price

            soldPrice = retrieveSalesItemPrice();

        }
    }


    public int retrieveSellingUnitPrice() {
        if (itemPriceAdjustment == null) {
            return sellingPrice.getPrice();
        } else {
            return calculateAdjustedPrice(sellingPrice == null ? 0 : sellingPrice.getPrice(), itemPriceAdjustment);
        }
    }

    public int retrieveSalesItemPrice() {
        return purchaseQty * retrieveSellingUnitPrice();
    }

    public int populateSoldPrice() {
        return soldPrice = retrieveSalesItemPrice();
    }

    private int calculateAdjustedPrice(int chargeAmount, ItemPriceAdjustment adjustment) {
        if (adjustment != null) {
            int adjustedValue = adjustment.getAdjustedValue();
            if (adjustedValue > 0) {
                if (adjustment.getPaymentType() == ItemPriceAdjustment.PaymentType.DOLLAR) {
                    chargeAmount = chargeAmount + adjustedValue;
                } else {
                    chargeAmount += Calculations.calculatePercentage(chargeAmount, adjustedValue);
                }
            } else {
                if (adjustment.getPaymentType() == ItemPriceAdjustment.PaymentType.DOLLAR) {
                    chargeAmount = chargeAmount - adjustedValue;
                } else {
                    chargeAmount -= Calculations.calculatePercentage(chargeAmount, adjustedValue);
                }
            }
        }
        return chargeAmount;
    }

    public String getInstruct() {
        return instruct;
    }

    public void setInstruct(String instruct) {
        this.instruct = instruct;
    }

    public int getDosage() {
        return dosage;
    }

    public void setDosage(int dosage) {
        this.dosage = dosage;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    public ItemPriceAdjustment getItemPriceAdjustment() {
        if (itemPriceAdjustment == null) itemPriceAdjustment = new ItemPriceAdjustment();
        return itemPriceAdjustment;
    }

    public void setItemPriceAdjustment(ItemPriceAdjustment itemPriceAdjustment) {
        this.itemPriceAdjustment = itemPriceAdjustment;
    }

    public Cost getCost() {
        if (cost == null) cost = new Cost();
        return cost;
    }

    public void setCost(Cost cost) {
        this.cost = cost;
    }

    public int getPurchaseQty() {
        return purchaseQty;
    }

    public void setPurchaseQty(int purchaseQty) {
        this.purchaseQty = purchaseQty;
    }

    public String getPurchaseUom() {
        return purchaseUom;
    }

    public void setPurchaseUom(String purchaseUom) {
        this.purchaseUom = purchaseUom;
    }


    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public SellingPrice getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(SellingPrice sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public void setSoldPrice(int soldPrice) {
        this.soldPrice = soldPrice;
    }

    public int getSoldPrice() {
        return soldPrice;
    }

    public String getItemRefId() {
        return itemRefId;
    }

    public void setItemRefId(String itemRefId) {
        this.itemRefId = itemRefId;
    }

    public Item.ItemType getItemType() {
        return itemType;
    }

    public void setItemType(Item.ItemType itemType) {
        this.itemType = itemType;
    }

    @Override
    public String toString() {
        return "SalesItem{" +
                ", itemRefId='" + itemRefId + '\'' +
                ", cost=" + cost +
                ", sellingPrice=" + sellingPrice +
                ", soldPrice=" + soldPrice +
                ", dosage=" + dosage +
                ", duration=" + duration +
                ", purchaseQty=" + purchaseQty +
                ", instruct='" + instruct + '\'' +
                ", batchNo='" + batchNo + '\'' +
                ", purchaseUom='" + purchaseUom + '\'' +
                ", expireDate=" + expireDate +
                ", remarks='" + remarks + '\'' +
                ", itemPriceAdjustment=" + itemPriceAdjustment +
                ", itemType=" + itemType +
                '}';
    }
}
