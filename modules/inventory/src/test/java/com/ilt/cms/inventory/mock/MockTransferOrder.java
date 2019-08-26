package com.ilt.cms.inventory.mock;

import com.ilt.cms.inventory.helper.TestHelper;
import com.ilt.cms.inventory.model.purchase.*;
import com.ilt.cms.inventory.model.purchase.enums.OrderStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MockTransferOrder {
    public static TransferOrder mockTransferOrder(){
        TransferOrder transferOrder = new TransferOrder("k23jsi83jmkls9343", "4jn609lf4h3224343",
                "392732322", LocalDateTime.now(), null, "dfd323432k3b", OrderStatus.APPROVED);
        transferOrder.setTransferRequestItems(Arrays.asList(
                mockTransferRequestItem("890j3iuj33n343", "BOX", 10),
                mockTransferRequestItem("0834n4884bb343", "BOX", 4)
        ));
        transferOrder.setGoodReceiveNotes(new ArrayList<>(Arrays.asList(
                mockGoodReceivedNote(LocalDate.now(), LocalDate.now(), "3094032k33", "00232", "remark")
        )));

        transferOrder.setDeliveryNotes(new ArrayList<>(Arrays.asList(
               mockDeliveryNote("4jn609lf4h3224343", "dfd323432k3b", LocalDate.now(), LocalDate.now(), "DN-993843")
        )));
        transferOrder.setGoodReceiveVoidNotes(new ArrayList<>(Arrays.asList(
                mockGoodReceivedVoidNote(LocalDate.now(), LocalDate.now(), "3094032k33", "00232", "remark")
        )));
        transferOrder.setDeliveryVoidNotes(new ArrayList<>(Arrays.asList(
                mockDeliveryVoidNote("jdjks4432432345522d3", "343k38jel39db3k3", LocalDate.now(), LocalDate.now(), "DVN-09991")
        )));

        return transferOrder;
    }

    public static GoodReceiveNote mockGoodReceivedNote(LocalDate createDate,LocalDate grnDate, String delivererId,
                                                       String grnNo, String additionalRemark){
        GoodReceiveNote goodReceiveNote = new GoodReceiveNote(createDate, grnDate, delivererId, grnNo, additionalRemark);
        goodReceiveNote.setReceivedItems(new ArrayList<>(Arrays.asList(
                mockGoodReceivedItem("0i9834834543o","299232l2j3h223","BOX", 10, "D-2322",
                        LocalDate.of(2020, 12, 31), "Drug is received", true),
                mockGoodReceivedItem("93425775433m443","84kl3h545354567","BOX", 10, "SU-2999",
                        LocalDate.of(2020, 12, 31), "Condition not good", false)
        )));
        return goodReceiveNote;
    }

    public static GoodReceivedItem mockGoodReceivedItem(String id, String itemRefId, String uom, int quantity,
                                                        String batchNumber, LocalDate expiryDate,
                                                        String comment, boolean isCountInStock){
        GoodReceivedItem goodReceivedItem = new GoodReceivedItem(id, itemRefId, uom, batchNumber, quantity, expiryDate, comment, isCountInStock);
        return goodReceivedItem;
    }

    public static TransferRequestItem mockTransferRequestItem(String itemRefId, String uom, int quantity){
        TransferRequestItem transferRequestItem = new TransferRequestItem(itemRefId, uom, quantity);
        return transferRequestItem;
    }

    public static DeliveryNote mockDeliveryNote(String requestClinicId, String senderClinicId, LocalDate createDate,
                                                LocalDate deliveryDate, String deliveryNoteNo){
        DeliveryNote deliveryNote = new DeliveryNote(requestClinicId, senderClinicId, createDate, deliveryDate, deliveryNoteNo);
        deliveryNote.setTransferSendItems(new ArrayList<>(Arrays.asList(
                mockTransferSendItem("89342038433859","43400322lfk3","BOX", "BN-22322",12, LocalDate.of(2020, 12, 31),
                        false)
        )));
        return deliveryNote;
    }

    public static TransferSendItem mockTransferSendItem(String id, String itemRefId, String uom, String batchNumber, int quantity,
                                                        LocalDate expiryDate, boolean isCountInStock){
        TransferSendItem transferSendItem = new TransferSendItem(id, itemRefId, uom, batchNumber,
                quantity, expiryDate, isCountInStock);
        return transferSendItem;
    }

    public static GoodReceiveVoidNote mockGoodReceivedVoidNote(LocalDate createDate, LocalDate grnDate, String delivererId,
                                                               String grnNo, String additionalRemark){
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

    public static DeliveryVoidNote mockDeliveryVoidNote(String itemDeliveryClinicId, String itemOwnerClinicId, LocalDate createDate,
                                                        LocalDate receivedDate, String dvnNo){
        DeliveryVoidNote deliveryVoidNote = new DeliveryVoidNote(itemDeliveryClinicId, itemOwnerClinicId, createDate, receivedDate, dvnNo);
        deliveryVoidNote.setTransferSendVoidItems(
                new ArrayList<>(Arrays.asList(
                        mockTransferSendVoidItem("5782993l332j3","299232l2j3h223","BOX",  "D-2322", 10,
                                LocalDate.of(2020, 12, 31) , true),
                        mockTransferSendVoidItem("9dkki309m32003", "2993j493k23j332","BOX",  "SU-2999", 10,
                                LocalDate.of(2020, 12, 31), false)
                )));
        return deliveryVoidNote;

    }

    public static TransferSendVoidItem mockTransferSendVoidItem(String id, String itemRefId, String uom, String batchNumber, int quantity,
                                                                LocalDate expiryDate, boolean isCountInStock){
        TransferSendVoidItem transferSendVoidItem = new TransferSendVoidItem(id, itemRefId, uom, batchNumber, quantity, expiryDate, isCountInStock);
        return transferSendVoidItem;
    }
}
