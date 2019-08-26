package com.ilt.cms.api.entity.bill;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DirectPayment {
    public enum PaymentMode {
        CASH, NETS, VISA, MASTER_CARD, AMERICAN_EXPRESS, JCB, OTHER_CREDIT_CARD, CHEQUE, GIRO
    }

    private double amount;
    private String patientVisitRegistrationId;
    private double totalGst;
    private double cashRoundAdjustedValue;
    private List<PaymentInfo> paymentInfos = new ArrayList<>();

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class PaymentInfo {
        private String billTransactionId;
        private PaymentMode billMode;
        private double amount;
        private String externalTransactionId;
        private String patientVisitRegistrationId;
    }
}
