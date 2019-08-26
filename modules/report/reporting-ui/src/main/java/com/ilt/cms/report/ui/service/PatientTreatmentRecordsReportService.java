package com.ilt.cms.report.ui.service;

import com.ilt.cms.core.entity.casem.Case;
import com.ilt.cms.core.entity.casem.Invoice;
import com.ilt.cms.core.entity.casem.SalesItem;
import com.ilt.cms.core.entity.casem.SalesOrder;
import com.ilt.cms.core.entity.coverage.CoveragePlan;
import com.ilt.cms.core.entity.doctor.Doctor;
import com.ilt.cms.core.entity.item.Item;
import com.ilt.cms.core.entity.patient.Patient;
import com.ilt.cms.core.entity.visit.PatientVisitRegistry;
import com.ilt.cms.report.ui.models.PatientTreatmentRecordsReport;
import com.ilt.cms.report.ui.models.dataset.PatientTreatmentItem;
import com.ilt.cms.report.ui.util.Formatter;
import org.apache.commons.lang.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PatientTreatmentRecordsReportService extends BirtReportServiceConfig {

    private static final Logger logger = LogManager.getLogger(PatientTreatmentRecordsReport.class);

    public List<PatientTreatmentRecordsReport> getPatientTreatmentRecord(List<String> clinicIds, List<String> doctorIds,
                                                                         List<String> financialPlanIds, List<String> itemCategoryCodes,
                                                                         List<String> itemCodes, LocalDateTime startDate, LocalDateTime endDate) {

        logger.info("Searching patient treatment data for clinicIds : " + clinicIds +
                " doctorIds: " + doctorIds + " financial plan ids: " + financialPlanIds +
                " item codes: " + itemCodes + " item category codes: " + itemCodes +
                " start time: " + itemCategoryCodes + " end time: " + itemCategoryCodes
        );

        List<PatientTreatmentRecordsReport> reports = new ArrayList<>();
        List<PatientVisitRegistry> visitRegistries = patientVisitRegistryDatabaseService
                .searchClinicAndDoctorsByStartTime(clinicIds, doctorIds, startDate, endDate);

        for (PatientVisitRegistry visitRegistry : visitRegistries) {
            PatientTreatmentRecordsReport report = new PatientTreatmentRecordsReport();
            if (visitRegistry.getCaseId() != null) {
                Case aCase = caseDatabaseService.findByCaseId(visitRegistry.getCaseId());
                report.feedCaseData(aCase.getCaseNumber());
                if (visitRegistry.getPreferredDoctorId() != null) {
                    Doctor doctor = doctorDatabaseService.findOne(visitRegistry.getPreferredDoctorId()).orElse(new Doctor());
                    report.feedDoctorData(doctor.getName());
                }
                Patient patient = patientDatabaseService.findPatientById(visitRegistry.getPatientId()).orElse(new Patient());
                report.feedPatientData(patient.getName(), patient.getPatientNumber(), patient.getUserId().getNumber());
                SalesOrder salesOrder = aCase.getSalesOrder();
                for (Invoice invoice : salesOrder.getInvoices()) {
                    report.setInvoiceNumber(report.getInvoiceNumber() + "\n" + invoice.getInvoiceNumber());
                }
                int chargeAmount = 0;
                List<PatientTreatmentItem> patientTreatmentItems = new ArrayList<>();
                for (SalesItem purchaseItem : salesOrder.getPurchaseItems()) {
                    PatientTreatmentRecordsReport treatmentRecordsReport = (PatientTreatmentRecordsReport) SerializationUtils.clone(report);
                    List<String> stringList = purchaseItem.receiveUsedPlans(aCase.getAttachedMedicalCoverages());
                    String medicalCoverageName = "";
                    for (String s : stringList) {
                        CoveragePlan plan = medicalCoverageDatabaseService.findPlan(s);
                        medicalCoverageName = ", " + plan.getName();
                    }
                    Item item = itemDatabaseService.findById(purchaseItem.getItemRefId()).orElse(new Item());
                    patientTreatmentItems.add(new PatientTreatmentItem(item.getName(), Formatter.currencyDecimalFormat(purchaseItem.getSoldPrice())));
                    treatmentRecordsReport.setCoverageName(medicalCoverageName.replaceFirst(", ", ""));
                    chargeAmount += purchaseItem.getSoldPrice();
                    treatmentRecordsReport.setChargedAmount(Formatter.currencyDecimalFormat(chargeAmount));
                    treatmentRecordsReport.setItems(patientTreatmentItems);
                    treatmentRecordsReport.setStartTime(Formatter.dateConvert(visitRegistry.getStartTime()));
                    reports.add(treatmentRecordsReport);
                }
            }
        }
        logger.info("Number of data rows found in report: " + reports.size());
        return reports;
    }

    public List<PatientTreatmentRecordsReport> getPatientTreatmentRecordReport() {

        List<PatientTreatmentRecordsReport> list = new ArrayList<>();
/*        List<PatientTreatmentItem> itemList1 = new ArrayList<>();
        List<PatientTreatmentItem> itemList2 = new ArrayList<>();

        PatientTreatmentItem item = new PatientTreatmentItem(1, "Name1", 10F);
        PatientTreatmentItem item2 = new PatientTreatmentItem(1, "Name2", 10F);
        PatientTreatmentItem item3 = new PatientTreatmentItem(3, "Name3", 10F);
        PatientTreatmentItem item4 = new PatientTreatmentItem(2, "Name4", 10F);
        PatientTreatmentItem item5 = new PatientTreatmentItem(2, "Name5", 10F);

        itemList1.add(item);
        itemList1.add(item2);
        itemList2.add(item3);
        itemList2.add(item4);
        itemList2.add(item5);

        PatientTreatmentRecordsReport report = new PatientTreatmentRecordsReport(1, "ItemName", 100F,
                "CoverageName", "CoverageType", "VisitId", new Date(),
                "VisitNumber", "PatientNumber", "PatientName",
                "PatientIdentity", "DoctorName", "BillNumber1",
                "CaseNumber", itemList1);
        PatientTreatmentRecordsReport report2 = new PatientTreatmentRecordsReport(2, "ItemName", 100F,
                "CoverageName", "CoverageType", "VisitId", new Date(),
                "VisitNumber", "PatientNumber", "PatientName",
                "PatientIdentity", "DoctorName", "BillNumber2",
                "CaseNumber", itemList1);
        PatientTreatmentRecordsReport report3 = new PatientTreatmentRecordsReport(3, "ItemName", 100F,
                "CoverageName", "CoverageType", "VisitId", new Date(),
                "VisitNumber", "PatientNumber", "PatientName",
                "PatientIdentity", "DoctorName", "BillNumber3",
                "CaseNumber", itemList2);

        list.add(report);
        list.add(report2);
        list.add(report3);*/
        return list;
    }
}
