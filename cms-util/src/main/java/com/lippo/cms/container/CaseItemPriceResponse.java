package com.lippo.cms.container;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lippo.cms.util.CMSConstant;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CaseItemPriceResponse {

    private int totalPaid;
    private int totalPrice;
    private List<ItemPrice> itemPrices = new ArrayList<>();

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class ItemPrice {

        public ItemPrice(String itemId, int unitPrice, int gstAmount) {
            this.itemId = itemId;
            this.unitPrice = unitPrice;
            this.gstAmount = gstAmount;
        }

        private String itemId;
        private int unitPrice;
        private int gstAmount;
        private String purchasedId;
        private List<InventoryData> inventoryData = new ArrayList<>();
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InventoryData {

        private String batchNo;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CMSConstant.JSON_DATE_FORMAT)
        @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
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
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
        @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
        private LocalDateTime createdAt;
        private int createdUserId;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
        @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
        private LocalDateTime updatedAt;
        private int updatedUserId;
        private String drugCode;

        private String itemId;
    }
}