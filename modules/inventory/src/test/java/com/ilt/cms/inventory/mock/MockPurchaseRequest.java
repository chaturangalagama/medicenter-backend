package com.ilt.cms.inventory.mock;

import com.ilt.cms.inventory.model.common.UOM;
import com.ilt.cms.inventory.model.common.UnitPrice;
import com.ilt.cms.inventory.model.purchase.PurchaseRequest;
import com.ilt.cms.inventory.model.purchase.RequestItem;
import com.ilt.cms.inventory.model.purchase.enums.RequestStatus;

import java.time.LocalDateTime;
import java.util.Arrays;

public class MockPurchaseRequest {

    public static PurchaseRequest mockPurchaseRequest(String id, RequestStatus requestStatus){
        PurchaseRequest purchaseRequest = new PurchaseRequest("C023232", "33212329943", LocalDateTime.now(),
                requestStatus, "S000212");
        purchaseRequest.setRequestItems(Arrays.asList(
                mockRequestItem(null,"22k32e30023223", "BOX", 10, new UnitPrice(12, false)),
                mockRequestItem(null,"434kknd99433n43", "BOX", 12, new UnitPrice(20, false))
        ));
        purchaseRequest.setId(id);
        return purchaseRequest;
    }

    public static RequestItem mockRequestItem(String supplierId, String itemRefId, String uom, int quantity, UnitPrice unitPrice){
        RequestItem requestItem = new RequestItem(itemRefId, null, uom, quantity, unitPrice);
        return requestItem;
    }
}
