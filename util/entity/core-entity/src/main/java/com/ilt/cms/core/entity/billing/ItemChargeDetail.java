package com.ilt.cms.core.entity.billing;

import com.ilt.cms.core.entity.sales.ItemPriceAdjustment;
import com.ilt.cms.core.entity.charge.Charge;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class ItemChargeDetail {

    private String itemId;
    private int quantity;
    private Charge charge;
    private ItemPriceAdjustment itemPriceAdjustment;
    private Set<String> excludedPlans;

    public ItemChargeDetail() {
    }

    public ItemChargeDetail(String itemId, int quantity, Charge charge, ItemPriceAdjustment itemPriceAdjustment) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.charge = charge;
        this.itemPriceAdjustment = itemPriceAdjustment;
        this.excludedPlans = excludedPlans;
    }

    public String getItemId() {
        return itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public ItemPriceAdjustment getItemPriceAdjustment() {
        return itemPriceAdjustment;
    }

    public Set<String> getExcludedPlans() {
        return excludedPlans;
    }

    public Charge getCharge() {
        return charge;
    }

    public int calculateAdjustedPrice() {
        if (itemPriceAdjustment != null) {
            return itemPriceAdjustment.calculateAdjustedPrice(charge.getPrice());
        }
        return charge.getPrice();
    }

    public static class ItemChargeRequest {
        private Map<String, Integer> planMaxUsage = new HashMap<>();
        private List<ItemChargeDetail> chargeDetails;

        public ItemChargeRequest() {
        }

        public ItemChargeRequest(Map<String, Integer> planMaxUsage, List<ItemChargeDetail> chargeDetails) {
            this.planMaxUsage = planMaxUsage;
            this.chargeDetails = chargeDetails;
        }

        public Map<String, Integer> getPlanMaxUsage() {
            return planMaxUsage;
        }

        public List<ItemChargeDetail> getChargeDetails() {
            return chargeDetails;
        }


        @Override
        public String toString() {
            return "ItemChargeRequest{" +
                    "planMaxUsage=" + planMaxUsage +
                    ", chargeDetails=" + chargeDetails +
                    '}';
        }
    }


    public static class ItemChargeDetailResponse {
        private Map<String, Integer> planMaxUsage = new HashMap<>();
        private List<ItemChargeDetail> chargeDetails;

        private List<InventoryData> inventoryData = new ArrayList<>();

        public ItemChargeDetailResponse() {
        }

        public ItemChargeDetailResponse(Map<String, Integer> planMaxUsage, List<ItemChargeDetail> chargeDetails) {
            this.planMaxUsage = planMaxUsage;
            this.chargeDetails = chargeDetails;
        }

        public Map<String, Integer> getPlanMaxUsage() {
            return planMaxUsage;
        }

        public List<ItemChargeDetail> getChargeDetails() {
            return chargeDetails;
        }

        public List<InventoryData> getInventoryData() {
            return inventoryData;
        }

        public void setInventoryData(List<InventoryData> inventoryData) {
            this.inventoryData = inventoryData;
        }


        public static class InventoryData {

            private String batchNo;
            private LocalDate expireDate;

            private int inventoryDetailId;
            private int inventoryId;
            private int clinicId;
            private int chargeId;
            private double quantity;
            private double quantityTotal;
            private double amount;
            private double itemAmount;
            private double itemCost;
            private double remainingQuantity;
            private double remainingAmount;
            private LocalDateTime createdAt;
            private int createdUserId;
            private LocalDateTime updatedAt;
            private int updatedUserId;
            private String drugCode;
            private double totalQuantity = 0;
            private double totalAmount = 0;
            private double cost;
            private boolean mixingFlag;

            private String itemId;
            private int inventoryUsageIndex;
            private int patientRegisterDetailId;

            public InventoryData() {
            }

            public InventoryData(String batchNo, LocalDate expireDate, int inventoryDetailId, int inventoryId, int clinicId,
                                 int chargeId, double quantity, double quantityTotal, double amount, double itemAmount,
                                 double itemCost, double remainingQuantity, double remainingAmount, LocalDateTime createdAt,
                                 int createdUserId, LocalDateTime updatedAt, int updatedUserId, String drugCode,
                                 double totalQuantity, double totalAmount, double cost, boolean mixingFlag, String itemId,
                                 int inventoryUsageIndex, int patientRegisterDetailId) {
                this.batchNo = batchNo;
                this.expireDate = expireDate;
                this.inventoryDetailId = inventoryDetailId;
                this.inventoryId = inventoryId;
                this.clinicId = clinicId;
                this.chargeId = chargeId;
                this.quantity = quantity;
                this.quantityTotal = quantityTotal;
                this.amount = amount;
                this.itemAmount = itemAmount;
                this.itemCost = itemCost;
                this.remainingQuantity = remainingQuantity;
                this.remainingAmount = remainingAmount;
                this.createdAt = createdAt;
                this.createdUserId = createdUserId;
                this.updatedAt = updatedAt;
                this.updatedUserId = updatedUserId;
                this.drugCode = drugCode;
                this.totalQuantity = totalQuantity;
                this.totalAmount = totalAmount;
                this.cost = cost;
                this.mixingFlag = mixingFlag;
                this.itemId = itemId;
                this.inventoryUsageIndex = inventoryUsageIndex;
                this.patientRegisterDetailId = patientRegisterDetailId;
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

            public int getInventoryDetailId() {
                return inventoryDetailId;
            }

            public void setInventoryDetailId(int inventoryDetailId) {
                this.inventoryDetailId = inventoryDetailId;
            }

            public int getInventoryId() {
                return inventoryId;
            }

            public void setInventoryId(int inventoryId) {
                this.inventoryId = inventoryId;
            }

            public int getClinicId() {
                return clinicId;
            }

            public void setClinicId(int clinicId) {
                this.clinicId = clinicId;
            }

            public int getChargeId() {
                return chargeId;
            }

            public void setChargeId(int chargeId) {
                this.chargeId = chargeId;
            }

            public double getQuantity() {
                return quantity;
            }

            public void setQuantity(double quantity) {
                this.quantity = quantity;
            }

            public double getQuantityTotal() {
                return quantityTotal;
            }

            public void setQuantityTotal(double quantityTotal) {
                this.quantityTotal = quantityTotal;
            }

            public double getAmount() {
                return amount;
            }

            public void setAmount(double amount) {
                this.amount = amount;
            }

            public double getItemAmount() {
                return itemAmount;
            }

            public void setItemAmount(double itemAmount) {
                this.itemAmount = itemAmount;
            }

            public double getItemCost() {
                return itemCost;
            }

            public void setItemCost(double itemCost) {
                this.itemCost = itemCost;
            }

            public double getRemainingQuantity() {
                return remainingQuantity;
            }

            public void setRemainingQuantity(double remainingQuantity) {
                this.remainingQuantity = remainingQuantity;
            }

            public double getRemainingAmount() {
                return remainingAmount;
            }

            public void setRemainingAmount(double remainingAmount) {
                this.remainingAmount = remainingAmount;
            }

            public LocalDateTime getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(LocalDateTime createdAt) {
                this.createdAt = createdAt;
            }

            public int getCreatedUserId() {
                return createdUserId;
            }

            public void setCreatedUserId(int createdUserId) {
                this.createdUserId = createdUserId;
            }

            public LocalDateTime getUpdatedAt() {
                return updatedAt;
            }

            public void setUpdatedAt(LocalDateTime updatedAt) {
                this.updatedAt = updatedAt;
            }

            public int getUpdatedUserId() {
                return updatedUserId;
            }

            public void setUpdatedUserId(int updatedUserId) {
                this.updatedUserId = updatedUserId;
            }

            public String getDrugCode() {
                return drugCode;
            }

            public void setDrugCode(String drugCode) {
                this.drugCode = drugCode;
            }

            public double getTotalQuantity() {
                return totalQuantity;
            }

            public void setTotalQuantity(double totalQuantity) {
                this.totalQuantity = totalQuantity;
            }

            public double getTotalAmount() {
                return totalAmount;
            }

            public void setTotalAmount(double totalAmount) {
                this.totalAmount = totalAmount;
            }

            public double getCost() {
                return cost;
            }

            public void setCost(double cost) {
                this.cost = cost;
            }

            public boolean isMixingFlag() {
                return mixingFlag;
            }

            public void setMixingFlag(boolean mixingFlag) {
                this.mixingFlag = mixingFlag;
            }

            public String getItemId() {
                return itemId;
            }

            public void setItemId(String itemId) {
                this.itemId = itemId;
            }

            public int getInventoryUsageIndex() {
                return inventoryUsageIndex;
            }

            public void setInventoryUsageIndex(int inventoryUsageIndex) {
                this.inventoryUsageIndex = inventoryUsageIndex;
            }

            public int getPatientRegisterDetailId() {
                return patientRegisterDetailId;
            }

            public void setPatientRegisterDetailId(int patientRegisterDetailId) {
                this.patientRegisterDetailId = patientRegisterDetailId;
            }

            @Override
            public String toString() {
                return "InventoryData{" +
                        "batchNo='" + batchNo + '\'' +
                        ", expireDate=" + expireDate +
                        ", inventoryDetailId=" + inventoryDetailId +
                        ", inventoryId=" + inventoryId +
                        ", clinicId=" + clinicId +
                        ", chargeId=" + chargeId +
                        ", quantity=" + quantity +
                        ", quantityTotal=" + quantityTotal +
                        ", amount=" + amount +
                        ", itemAmount=" + itemAmount +
                        ", itemCost=" + itemCost +
                        ", remainingQuantity=" + remainingQuantity +
                        ", remainingAmount=" + remainingAmount +
                        ", createdAt=" + createdAt +
                        ", createdUserId=" + createdUserId +
                        ", updatedAt=" + updatedAt +
                        ", updatedUserId=" + updatedUserId +
                        ", drugCode='" + drugCode + '\'' +
                        ", totalQuantity=" + totalQuantity +
                        ", totalAmount=" + totalAmount +
                        ", cost=" + cost +
                        ", mixingFlag=" + mixingFlag +
                        ", itemId='" + itemId + '\'' +
                        ", inventoryUsageIndex=" + inventoryUsageIndex +
                        ", patientRegisterDetailId=" + patientRegisterDetailId +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "ItemChargeDetailResponse{" +
                    "planMaxUsage=" + planMaxUsage +
                    ", chargeDetails=" + chargeDetails +
                    '}';
        }
    }


    @Override
    public String toString() {
        return "ItemChargeDetail{" +
                "itemId='" + itemId + '\'' +
                ", quantity=" + quantity +
                ", charge=" + charge +
                ", itemPriceAdjustment=" + itemPriceAdjustment +
                ", excludedPlans=" + excludedPlans +
                '}';
    }
}
