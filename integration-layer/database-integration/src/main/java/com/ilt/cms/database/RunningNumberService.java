package com.ilt.cms.database;

public interface RunningNumberService {

    String generateSalesOrderNumber();

    String generateInvoiceNumber();

    String generatePatientNumber();

    String generateBillNumber();

    String generateVisitNumber();

    String generateAdjustmentNumber();

    String generateMedicalCertificateNumber();

    String generateRequestNumber();

    String generateOrderNumber();

    String generateGRNNumber();

    String generateGRVNNumber();

    String generateDeliveryNote();

    String generateDeliveryVoidNote();

    long queueNextNumber(String clinicCode, byte prefix);

}
