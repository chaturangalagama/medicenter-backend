package com.ilt.cms.inventory.model.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.inventory.model.purchase.enums.RequestStatus;
import com.lippo.cms.util.CMSConstant;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Request extends PersistedObject {

    protected String requestClinicId;

    @Indexed(unique = true)
    protected String requestNo;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    protected LocalDateTime requestTime;

    protected List<RequestItem> requestItems;

    protected RequestStatus requestStatus;

    public abstract boolean checkValidate();

    public Request() {
        requestItems = new ArrayList<>();
    }

    public Request(String requestClinicId, String requestNo, LocalDateTime requestTime, RequestStatus requestStatus) {
        this.requestClinicId = requestClinicId;
        this.requestNo = requestNo;
        this.requestTime = requestTime;
        this.requestStatus = requestStatus;
    }

    public String getRequestClinicId() {
        return requestClinicId;
    }

    public void setRequestClinicId(String requestClinicId) {
        this.requestClinicId = requestClinicId;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public List<RequestItem> getRequestItems() {
        return requestItems;
    }

    public void setRequestItems(List<RequestItem> requestItems) {
        this.requestItems = requestItems;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }
}
