package com.ilt.cms.api.entity.sales;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class SalesOrderEntity {
    public enum SalesStatus {
        OPEN, CLOSED
    }

    private int taxValue;
    private String salesRefNo;
    private List<SalesItemEntity> purchaseItem = new ArrayList<>();
    private List<InvoiceEntity> invoices = new ArrayList<>();
    private SalesStatus status;
    private int totalPrice;
    private int totalPayableTax;
    private int totalPaid;
    private int outstanding;
}
