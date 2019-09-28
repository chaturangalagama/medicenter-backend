package com.ilt.cms.api.entity.sales;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lippo.cms.util.CMSConstant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class SalesItemEntity {

    private String itemRefId;
    private int originalTotalPrice;
    private List<SalesItemEntity> subItems = new ArrayList<>();
    private CostEntity cost = new CostEntity();
    private SellingPriceEntity unitPrice = new SellingPriceEntity();
    private int dosage;
    private int duration;
    private int purchaseQty;
    private String instruct;
    private String batchNo;
    private String purchaseUom;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CMSConstant.JSON_DATE_FORMAT)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
    private LocalDate expireDate;
    private ItemPriceAdjustmentEntity priceAdjustment;
    private String remarks;

}
