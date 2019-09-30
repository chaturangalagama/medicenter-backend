package com.ilt.cms.core.entity.sales;

import com.ilt.cms.core.entity.PersistedObject;

import java.time.LocalDateTime;

public class Expense extends PersistedObject {

    private String description;
    private String expenseNumber;
    private LocalDateTime dateTime;
    private String type;
    private double amount;
    private double gstAmount;
    private String invoiceNumber;
}
