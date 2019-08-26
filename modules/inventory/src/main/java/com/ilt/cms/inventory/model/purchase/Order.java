package com.ilt.cms.inventory.model.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.inventory.model.purchase.enums.OrderStatus;
import com.lippo.cms.util.CMSConstant;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public abstract class Order extends PersistedObject {

    protected String requestId;

    protected String requestClinicId;

    @Indexed(unique = true)
    protected String orderNo;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    protected LocalDateTime orderTime;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    protected LocalDateTime completeTime;

    protected OrderStatus orderStatus;

    public abstract boolean checkValidate();

    public Order() {
    }

    public Order(String requestId, String requestClinicId, String orderNo, LocalDateTime orderTime, LocalDateTime completeTime, OrderStatus orderStatus) {
        this.requestId = requestId;
        this.requestClinicId = requestClinicId;
        this.orderNo = orderNo;
        this.orderTime = orderTime;
        this.completeTime = completeTime;
        this.orderStatus = orderStatus;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestClinicId() {
        return requestClinicId;
    }

    public void setRequestClinicId(String requestClinicId) {
        this.requestClinicId = requestClinicId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public LocalDateTime getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(LocalDateTime completeTime) {
        this.completeTime = completeTime;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
