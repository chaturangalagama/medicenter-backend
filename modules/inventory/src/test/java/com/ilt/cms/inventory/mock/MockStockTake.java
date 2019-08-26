package com.ilt.cms.inventory.mock;

import com.ilt.cms.inventory.model.inventory.StockCountItem;
import com.ilt.cms.inventory.model.inventory.StockTake;
import com.ilt.cms.inventory.model.inventory.enums.ItemRange;
import com.ilt.cms.inventory.model.inventory.enums.StockCountType;
import com.ilt.cms.inventory.model.inventory.enums.StockTakeApproveStatus;
import com.ilt.cms.inventory.model.inventory.enums.StockTakeStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MockStockTake {

    public static StockTake mockStockTake(){
         return mockStockTake("", LocalDate.of(2018, 11, 23), LocalTime.now(), "dkd33434mn43", StockCountType.PARTIAL,
                 ItemRange.A_E.name(), "CountName",StockTakeStatus.IN_PROCESS_FIRST_STOCK_TAKE, null,
         new ArrayList<>(Arrays.asList(
                 mockStockCountItem("huhb39934324k3", "C-00001", "item01", "DRUG","9232030ii99966", "ND2211",
                        "BOX",LocalDate.of(2030, 12, 31),"ND2211", "TAB",
                        LocalDate.of(2030, 12, 31), 100,99.0, null, "mistake to count"),
                 mockStockCountItem("nkhnjk3343bj43", "C-00002", "item02", "SERVICE","9232030i4366", "ND2211",
                         "BOX",LocalDate.of(2030, 12, 31), "ND2211","TAB",
                         LocalDate.of(2030, 12, 31),422, 390.0, null,"mistake to count")
                 )));
    }

    public static StockTake mockStockTake(StockCountType stockCountType, ItemRange itemRange, StockTakeStatus stockTakeStatus, StockTakeApproveStatus stockTakeApproveStatus,
                                          int availableCount, Double firstQuantity, Double secondaryQuantity){
        return mockStockTake("", LocalDate.of(2018, 11, 23), LocalTime.now(), "dkd33434mn43", stockCountType,
                itemRange.name(), "CountName",stockTakeStatus, stockTakeApproveStatus,
                new ArrayList<>(Arrays.asList(
                        mockStockCountItem("huhb39934324k3", "C-00001", "item01", "DRUG","9232030ii99966", "ND2211",
                                "BOX",LocalDate.of(2030, 12, 31),"ND2211", "TAB",
                                LocalDate.of(2030, 12, 31), 100,99.0, null, "mistake to count"),
                        mockStockCountItem("nkhnjk3343bj43", "C-00002", "item02", "SERVICE","9232030i4366", "ND2211",
                                "BOX",LocalDate.of(2030, 12, 31), "ND2211","TAB",
                                LocalDate.of(2030, 12, 31),422, 390.0, null,"mistake to count")
                )));
    }

    public static StockTake mockStockTake(String stockTakeName, LocalDate startDate, LocalTime startTime, String clinicId, StockCountType countType,
                                          String itemRange, String countName, StockTakeStatus stockTakeStatus, StockTakeApproveStatus stockTakeApproveStatus,
                                          List<StockCountItem> stockCountItems){
        StockTake stockTake = new StockTake(stockTakeName, startDate, startTime, clinicId, countType, itemRange, countName, stockTakeStatus, stockTakeApproveStatus);
        stockTake.setStockCountItems(stockCountItems);
        return stockTake;
    }

    public static StockCountItem mockStockCountItem(String inventoryId, String itemCode, String itemName, String itemType, String itemRefId, String curBatchNumber, String curUom, LocalDate curExpiryDate, String batchNumber, String uom, LocalDate expiryDate,
                                                    int availableCount, Double firstQuantity, Double secondaryQuantity, String reason){
        StockCountItem stockCountItem = new StockCountItem(inventoryId, itemCode, itemName, itemRefId, curBatchNumber, curUom, curExpiryDate,
        availableCount);
        stockCountItem.setBatchNumber(batchNumber);
        stockCountItem.setExpiryDate(expiryDate);
        stockCountItem.setBaseUom(uom);
        stockCountItem.setFirstQuantity(firstQuantity);
        stockCountItem.setSecondQuantity(secondaryQuantity);
        stockCountItem.setReason(reason);

        return stockCountItem;
    }

}
