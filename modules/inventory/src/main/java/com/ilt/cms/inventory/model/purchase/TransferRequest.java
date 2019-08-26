package com.ilt.cms.inventory.model.purchase;

import com.ilt.cms.inventory.model.purchase.enums.RequestStatus;

import java.time.LocalDateTime;
import java.util.List;

import static com.lippo.commons.util.CommonUtils.isStringValid;

public class TransferRequest extends Request {

    private String senderClinicId;

    private String transferNote;

    @Override
    public boolean checkValidate() {
        switch (requestStatus){
            case DRAFT:
                return isStringValid(requestClinicId, senderClinicId) && requestStatus != null;
            case REQUESTED:
                return isStringValid(requestClinicId, senderClinicId) && requestStatus != null && requestItems != null && requestItems.size() > 0;
            case APPROVED:
                default:
                    return true;
        }
    }

    public TransferRequest() {
    }

    public TransferRequest(String requestClinicId, String requestNo, LocalDateTime requestTime,
                           RequestStatus requestStatus, String senderClinicId, String transferNote) {
        super(requestClinicId, requestNo, requestTime, requestStatus);
        this.senderClinicId = senderClinicId;
        this.transferNote = transferNote;
    }

    public String getSenderClinicId() {
        return senderClinicId;
    }

    public void setSenderClinicId(String senderClinicId) {
        this.senderClinicId = senderClinicId;
    }

    public String getTransferNote() {
        return transferNote;
    }

    public void setTransferNote(String transferNote) {
        this.transferNote = transferNote;
    }

    @Override
    public String toString() {
        return "TransferRequest{" +
                "senderClinicId='" + senderClinicId + '\'' +
                ", transferNote='" + transferNote + '\'' +
                ", requestClinicId='" + requestClinicId + '\'' +
                ", requestNo='" + requestNo + '\'' +
                ", requestTime=" + requestTime +
                ", requestItems=" + requestItems +
                ", requestStatus=" + requestStatus +
                ", id='" + id + '\'' +
                '}';
    }
}
