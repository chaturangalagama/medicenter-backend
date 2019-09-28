package com.ilt.cms.core.entity.sales;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The purchased set of items, which we noted as Package is representing via this model.
 * Package items may be payable by Pre-paid, Post-paid or at the time of consume.
 */
public class Package {

    public enum PackageStatus {
        ON_GOING, COMPLETED
    }
    private String itemRefId;
    private String code;
    private String name;
    private int packageQty;
    private int purchasePrice;
    private LocalDateTime purchaseDate;
    private LocalDateTime expireDate;
    private PackageStatus status;
    private List<Dispatch> dispatches = new ArrayList<>();

    public List<Dispatch> getDispatches() {
        if (dispatches == null) dispatches = new ArrayList<>();
        return dispatches;
    }

    public void setDispatches(List<Dispatch> dispatches) {
        this.dispatches = dispatches;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(int purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getItemRefId() {
        return itemRefId;
    }

    public void setItemRefId(String itemRefId) {
        this.itemRefId = itemRefId;
    }

    public int getPackageQty() {
        return packageQty;
    }

    public void setPackageQty(int packageQty) {
        this.packageQty = packageQty;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }

    public PackageStatus getStatus() {
        return status;
    }

    public void setStatus(PackageStatus status) {
        this.status = status;
    }

    public void feedUpdate(Package aPackage) {
        int utilizedCount = 0;
        for (Dispatch dispatch : dispatches) {
            for (Dispatch updateDispatch : aPackage.getDispatches()) {
                if (dispatch.getItemCode().equals(updateDispatch.getItemCode())) {
                    if (updateDispatch.isPayable()) {
                        dispatch.setPayable(true);
                    }
                    if (updateDispatch.isUtilize()) {
                        dispatch.setUtilize(true);
                        dispatch.setPayable(true);
                        dispatch.setUtilizedDate(LocalDateTime.now());
                        utilizedCount++;
                    }
                    break;
                }
            }
        }
        if (utilizedCount == dispatches.size()) {
            this.setStatus(PackageStatus.COMPLETED);
        } else {
            this.setStatus(PackageStatus.ON_GOING);
        }
    }

    @Override
    public String toString() {
        return "Package{" +
                "itemRefId='" + itemRefId + '\'' +
                ", packageQty=" + packageQty +
                ", purchasePrice=" + purchasePrice +
                ", purchaseDate=" + purchaseDate +
                ", expireDate=" + expireDate +
                ", status=" + status +
                ", dispatches=" + dispatches +
                '}';
    }
}
