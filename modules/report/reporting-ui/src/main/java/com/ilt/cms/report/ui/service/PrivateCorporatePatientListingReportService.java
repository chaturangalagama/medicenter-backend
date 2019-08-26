package com.ilt.cms.report.ui.service;

import com.ilt.cms.report.ui.models.PrivateCorporatePatientListingReport;
import com.ilt.cms.report.ui.models.dataset.PrivateCorporatePatientListingItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrivateCorporatePatientListingReportService extends BirtReportServiceConfig {

    public List<PrivateCorporatePatientListingReport> getReportData() {

        List<PrivateCorporatePatientListingReport> list = new ArrayList<>();

        List<PrivateCorporatePatientListingItem> itemList1 = new ArrayList<>();
        PrivateCorporatePatientListingItem item1 = new PrivateCorporatePatientListingItem("Charge1", 150F, 80F);
        PrivateCorporatePatientListingItem item2 = new PrivateCorporatePatientListingItem("Charge2", 100F, 880F);
        PrivateCorporatePatientListingItem item3 = new PrivateCorporatePatientListingItem("Charge3", 750F, 540F);
        itemList1.add(item1);
        itemList1.add(item2);
        itemList1.add(item3);

        PrivateCorporatePatientListingReport report1 = new PrivateCorporatePatientListingReport(new Date(), "VisitNumber", "PatientNumber",
                "PatientName", "PatientIdentity", "DoctorName", "financialClass", "MedicalClass", "CoveragePlan",
                "CoveragePlanID", "PatientAddress", "Diagnosis", itemList1, "DateRange", "InvoiceNumber", "PaymentStatus", 1782F, 800F, 200F, 50F, 780F, "ClincCode", "ClinicGroup");
        PrivateCorporatePatientListingReport report2 = new PrivateCorporatePatientListingReport(new Date(), "VisitNumber2", "PatientNumber2",
                "PatientName2", "PatientIdentity2", "DoctorName2", "financialClass2", "MedicalClass2", "CoveragePlan2",
                "CoveragePlanID2", "PatientAddress2", "Diagnosis2", itemList1, "DateRange2", "InvoiceNumber2", "PaymentStatus2", 5682F, 500F, 288F, 150F, 7080F, "ClincCode", "ClinicGroup");

        list.add(report1);
        list.add(report2);
        return list;
    }
}
