package com.ilt.cms.inventory.mock;

import com.ilt.cms.inventory.model.common.UnitPrice;
import com.ilt.cms.inventory.model.purchase.*;
import com.ilt.cms.inventory.model.purchase.enums.OrderStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class MockPurchaseOrder {

    public static PurchaseOrder mockPurchaseOrder(){
        PurchaseOrder purchaseOrder = new PurchaseOrder("k23jsi83jmkls9343", "4jn609lf4h3224343",
                "392732322", LocalDateTime.now(), null, "dfd323432k3b", OrderStatus.APPROVED);
        purchaseOrder.setGoodPurchasedItems(new ArrayList<>(Arrays.asList(
                mockGoodOrderItem("890j3iuj33n343","BOX", 10, new UnitPrice(2000, false)),
                mockGoodOrderItem("0834n4884bb343","BOX", 4, new UnitPrice(120, false))
        )));
        purchaseOrder.setGoodReceiveNotes(new ArrayList<>(Arrays.asList(
                mockGoodReceivedNote(LocalDate.now(), LocalDate.now(), "3094032k33", "00232", "remark")
        )));
        purchaseOrder.setGoodReceiveVoidNotes(new ArrayList<>(Arrays.asList(
                mockGoodReceivedVoidNote(LocalDate.now(), LocalDate.now(), "3094032k33", "00232", "remark")
        )));
        return purchaseOrder;
    }

    public static GoodReceiveNote mockGoodReceivedNote(LocalDate createDate,LocalDate grnDate, String delivererId, String grnNo, String additionalRemark){
        GoodReceiveNote goodReceiveNote = new GoodReceiveNote(createDate, grnDate, delivererId, grnNo, additionalRemark);
        goodReceiveNote.setReceivedItems(new ArrayList<>(Arrays.asList(
                mockGoodReceivedItem("5782993l332j3","299232l2j3h223","BOX", 10, "D-2322",
                        LocalDate.of(2020, 12, 31), "Drug is received", true),
                mockGoodReceivedItem("9dkki309m32003", "2993j493k23j332","BOX", 10, "SU-2999",
                        LocalDate.of(2020, 12, 31), "Condition not good", false)
        )));
        return goodReceiveNote;
    }

    public static GoodReceivedItem mockGoodReceivedItem(String id, String itemRefId, String uom, int quantity, String batchNumber,
                                                        LocalDate expiryDate, String comment, boolean isCountInStock){
        GoodReceivedItem goodReceivedItem = new GoodReceivedItem(id, itemRefId, uom, batchNumber, quantity, expiryDate, comment, isCountInStock);
        return goodReceivedItem;
    }

    public static GoodOrderedItem mockGoodOrderItem(String itemRefId, String uom, int quantity, UnitPrice unitPrice){
        GoodOrderedItem goodOrderedItem = new GoodOrderedItem(itemRefId, uom, quantity, unitPrice);
        return goodOrderedItem;
    }

    public static GoodReceiveVoidNote mockGoodReceivedVoidNote(LocalDate createDate, LocalDate grnDate, String delivererId, String grnNo, String additionalRemark){
        GoodReceiveVoidNote goodReceiveVoidNote = new GoodReceiveVoidNote(createDate, grnDate, delivererId, grnNo, additionalRemark);
        goodReceiveVoidNote.setReturnItems(new ArrayList<>(Arrays.asList(
                mockGoodReturnItem("5782993l332j3","299232l2j3h223","BOX", 10, "D-2322",
                        LocalDate.of(2020, 12, 31),
                         "Drug is received", true),
                mockGoodReturnItem("9dkki309m32003", "2993j493k23j332","BOX", 10, "SU-2999",
                        LocalDate.of(2020, 12, 31),
                        "Condition not good", false)
        )));
        return goodReceiveVoidNote;
    }

    public static GoodReturnItem mockGoodReturnItem(String id, String itemRefId, String uom, int quantity, String batchNumber,
                                                    LocalDate expiryDate, String comment, boolean isCountInStock){
        GoodReturnItem goodReturnItem = new GoodReturnItem(id, itemRefId, uom, batchNumber, quantity, expiryDate, comment, isCountInStock);
        return goodReturnItem;
    }
}
