package com.ilt.cms.api.entity.medical;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ilt.cms.api.entity.sales.ItemPriceAdjustmentEntity;
import com.lippo.cms.util.CMSConstant;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DispatchItemEntity {

    private String purchasedId;
    private String itemId;
    private String dosageUom;
    private String dosageInstruction;
    private String instruct;
    private int duration;
    private int dosage;
    private int quantity;
    private int oriTotalPrice;
    private String batchNo;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
    private LocalDate expiryDate;
    private String remarks;
    private ItemPriceAdjustmentEntity itemPriceAdjustment;

    //No need to map this filed used only for sent data
    private String itemCode;
    private String itemName;

    public DispatchItemEntity(String purchasedId, String itemId, String dosageUom, String instruct, int duration, int dosage,
                              int quantity, int oriTotalPrice, String batchNo,
                              LocalDate expiryDate, ItemPriceAdjustmentEntity itemPriceAdjustment, String remarks, String dosageInstruction) {
        this.purchasedId = purchasedId;
        this.itemId = itemId;
        this.dosageUom = dosageUom;
        this.instruct = instruct;
        this.duration = duration;
        this.dosage = dosage;
        this.quantity = quantity;
        this.oriTotalPrice = oriTotalPrice;
        this.batchNo = batchNo;
        this.expiryDate = expiryDate;
        this.itemPriceAdjustment = itemPriceAdjustment;
        this.remarks = remarks;
        this.dosageInstruction = dosageInstruction;
    }
}
