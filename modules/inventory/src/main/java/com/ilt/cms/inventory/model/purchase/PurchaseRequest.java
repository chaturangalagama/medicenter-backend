package com.ilt.cms.inventory.model.purchase;

import com.ilt.cms.inventory.model.purchase.enums.RequestStatus;

import java.time.LocalDateTime;

import static com.lippo.commons.util.CommonUtils.isStringValid;

public class PurchaseRequest extends Request {

    private String supplierId;


    @Override
    public boolean checkValidate() {
        return isStringValid(requestClinicId, supplierId) && requestStatus != null;
    }

    public PurchaseRequest() {
    }

    public PurchaseRequest(String requestClinicId, String requestNo, LocalDateTime requestTime,
                           RequestStatus requestStatus, String supplierId) {
        super(requestClinicId, requestNo, requestTime, requestStatus);
        this.supplierId = supplierId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    @Override
    public String toString() {
        return "PurchaseRequest{" +
                "supplierId='" + supplierId + '\'' +
                ", requestClinicId='" + requestClinicId + '\'' +
                ", requestNo='" + requestNo + '\'' +
                ", requestTime=" + requestTime +
                ", requestItems=" + requestItems +
                ", requestStatus=" + requestStatus +
                ", id='" + id + '\'' +
                '}';
    }
}
