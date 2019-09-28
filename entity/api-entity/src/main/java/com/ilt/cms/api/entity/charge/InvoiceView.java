package com.ilt.cms.api.entity.charge;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ilt.cms.core.entity.sales.PaymentInfo;
import com.lippo.cms.util.CMSConstant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * <code>{@link InvoiceView}</code> -
 * API response entity for invoice.
 * </p>
 *
 * @author prabath.
 */
@Getter
@Setter
@ToString
public class InvoiceView {


    public enum InvoiceType {
        NON_CLAIMABLE, CLAIMABLE
    }

    private String invoiceId;

    private List<PaymentInfo> paymentInfos;

    private String invoiceState;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    private LocalDateTime invoiceTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    protected LocalDateTime paidTime;

    private InvoiceType invoiceType;

    private int payableAmount;

    private int paidAmount;

    private int includedTaxAmount;

    private String planId;
}
