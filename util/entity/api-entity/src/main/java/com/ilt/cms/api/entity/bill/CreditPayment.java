package com.ilt.cms.api.entity.bill;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CreditPayment {
    private String costCenter;
    private String billTransactionId;
    private double amount;
    private String patientVisitRegistrationId;
    private double totalGst;
}
