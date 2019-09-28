package com.ilt.cms.api.entity.sales;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ilt.cms.core.entity.sales.Invoice;
import com.ilt.cms.core.entity.sales.PaymentInfo;
import com.lippo.cms.util.CMSConstant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class InvoiceEntity {



    private String invoiceId;
    private Invoice.PaymentMode paymentMode;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern=CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    private LocalDateTime invoiceTime;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    private LocalDateTime paidTime;
    private int payableAmount;
    private int paidAmount;
    private int includedTaxAmount;
    private String planId;
    private String reference;
    private Invoice.InvoiceState invoiceState;
    private Invoice.InvoiceType invoiceType;
    private List<PaymentInfo> paymentInfos;

}
