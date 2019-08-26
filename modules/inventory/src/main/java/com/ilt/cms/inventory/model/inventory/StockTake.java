package com.ilt.cms.inventory.model.inventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.inventory.model.inventory.enums.StockCountType;
import com.ilt.cms.inventory.model.inventory.enums.StockTakeApproveStatus;
import com.ilt.cms.inventory.model.inventory.enums.StockTakeStatus;
import com.ilt.cms.inventory.service.inventory.StockTakeService;
import com.lippo.cms.util.CMSConstant;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.lippo.commons.util.CommonUtils.isStringValid;

public class StockTake extends PersistedObject {

    @Indexed(unique = true)
    private String stockTakeName;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
    private LocalDate startDate;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_TIME_FORMAT_WITH_SECONDS)
    @DateTimeFormat(pattern = CMSConstant.JSON_TIME_FORMAT_WITH_SECONDS)
    private LocalTime startTime;

    private String clinicId;

    private StockCountType countType;

    private String itemRange;

    private String countName;

    private StockTakeStatus stockTakeStatus;

    private StockTakeApproveStatus approveStatus;

    private List<StockCountItem> stockCountItems;

    public boolean checkValidate(){
        return isStringValid(stockTakeName, clinicId, countName) && countType != null && stockTakeStatus != null
                && approveStatus != null;
    }

    public void addStockCountItem(StockCountItem stockCountItem){
        stockCountItems.add(stockCountItem);
    }
    public StockTake() {
        stockCountItems = new ArrayList<>();
    }

    public StockTake(LocalDate startDate, LocalTime startTime, StockCountType countType, String itemRange,
                     String countName, StockTakeStatus stockTakeStatus) {
        this.startDate = startDate;
        this.startTime = startTime;
        this.countType = countType;
        this.itemRange = itemRange;
        this.countName = countName;
        this.stockTakeStatus = stockTakeStatus;
        this.stockCountItems = new ArrayList<>();
    }

    public StockTake(String stockTakeName, LocalDate startDate, LocalTime startTime, String clinicId,
                     StockCountType countType, String itemRange, String countName, StockTakeStatus stockTakeStatus,
                     StockTakeApproveStatus approveStatus) {
        this.stockTakeName = stockTakeName;
        this.startDate = startDate;
        this.startTime = startTime;
        this.clinicId = clinicId;
        this.countType = countType;
        this.itemRange = itemRange;
        this.countName = countName;
        this.stockTakeStatus = stockTakeStatus;
        this.approveStatus = approveStatus;
        this.stockCountItems = new ArrayList<>();
    }

    public String getStockTakeName() {
        return stockTakeName;
    }

    public void setStockTakeName(String stockTakeName) {
        this.stockTakeName = stockTakeName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public String getClinicId() {
        return clinicId;
    }

    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }

    public StockCountType getCountType() {
        return countType;
    }

    public void setCountType(StockCountType countType) {
        this.countType = countType;
    }

    public String getItemRange() {
        return itemRange;
    }

    public void setItemRange(String itemRange) {
        this.itemRange = itemRange;
    }

    public String getCountName() {
        return countName;
    }

    public void setCountName(String countName) {
        this.countName = countName;
    }

    public StockTakeStatus getStockTakeStatus() {
        return stockTakeStatus;
    }

    public void setStockTakeStatus(StockTakeStatus stockTakeStatus) {
        this.stockTakeStatus = stockTakeStatus;
    }

    public List<StockCountItem> getStockCountItems() {
        return stockCountItems;
    }

    public void setStockCountItems(List<StockCountItem> stockCountItems) {
        this.stockCountItems = stockCountItems;
    }

    public StockTakeApproveStatus getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(StockTakeApproveStatus approveStatus) {
        this.approveStatus = approveStatus;
    }

    @Override
    public String toString() {
        return "StockTake{" +
                "stockTakeName='" + stockTakeName + '\'' +
                ", startDate=" + startDate +
                ", startTime=" + startTime +
                ", clinicId='" + clinicId + '\'' +
                ", countType=" + countType +
                ", itemRange='" + itemRange + '\'' +
                ", countName='" + countName + '\'' +
                ", stockTakeStatus=" + stockTakeStatus +
                ", approveStatus=" + approveStatus +
                ", stockCountItems=" + stockCountItems +
                ", id='" + id + '\'' +
                '}';
    }
}
