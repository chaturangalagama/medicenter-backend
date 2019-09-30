package com.ilt.cms.api.entity.billPayment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ilt.cms.api.entity.bill.CreditPayment;
import com.ilt.cms.api.entity.bill.DirectPayment;
import com.lippo.cms.util.CMSConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BillPaymentEntity {
    public enum PaymentStatus {
        PENDING_PAYMENT_CONFIRMATION, PAID
    }

    public enum PaymentMode {
        SELF_PAY, COVERAGE, PAY_AT_CLINIC
    }

    private String id;
    private PaymentStatus paymentStatus;
    private String patientVisitId;
    private String patientId;
    private String clinicId;
    private String billNumber;
    private double gstValue;

    private double totalBillAmount;
    private double includedTotalGstAmount;

    private List<PaymentItem> drugs = new ArrayList<>();
    private List<PaymentItem> medicalServices = new ArrayList<>();
    private List<PaymentItem> medicalTests = new ArrayList<>();
    private List<PaymentItem> vaccinations = new ArrayList<>();

    private List<Adjustment> adjustments = new ArrayList<>();

    private DirectPayment directPayments = new DirectPayment();
    private List<CreditPayment> creditPayments = new ArrayList<>();

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    private LocalDateTime billPaymentTime;
}
