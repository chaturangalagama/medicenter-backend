package com.ilt.cms.inventory.mock;

import com.ilt.cms.inventory.model.common.UOM;
import com.ilt.cms.inventory.model.purchase.RequestItem;
import com.ilt.cms.inventory.model.purchase.TransferRequest;
import com.ilt.cms.inventory.model.purchase.enums.RequestStatus;

import java.time.LocalDateTime;
import java.util.Arrays;

public class MockTransferRequest {
    public static TransferRequest mockTransferRequest(String id, RequestStatus requestStatus){
        TransferRequest transferRequest = new TransferRequest("C023232", "33212329943", LocalDateTime.now(),
                requestStatus, "S000212", "Please deliver ASAP");
        transferRequest.setRequestItems(Arrays.asList(
                mockRequestItem("22k32e30023223", "TABLET", 10),
                mockRequestItem("22k32e30sfe33223", "BOX", 12)
        ));
        transferRequest.setId(id);
        return transferRequest;
    }

    public static RequestItem mockRequestItem(String itemRefId, String uom, int quantity){
        RequestItem requestItem = new RequestItem(itemRefId, uom, quantity);
        return requestItem;
    }
}
