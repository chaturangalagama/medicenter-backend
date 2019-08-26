package com.ilt.cms.report.ui.service;

import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.core.entity.casem.*;
import com.ilt.cms.core.entity.casem.Invoice.InvoiceState;
import com.ilt.cms.core.entity.casem.Invoice.InvoiceType;
import com.ilt.cms.core.entity.coverage.CoveragePlan;
import com.ilt.cms.core.entity.doctor.Doctor;
import com.ilt.cms.core.entity.patient.Patient;
import com.ilt.cms.core.entity.visit.PatientVisitRegistry;
import com.ilt.cms.report.ui.models.DailyQueueListReport;
import com.ilt.cms.report.ui.models.dataset.DailyQueueListItem;
import com.ilt.cms.report.ui.util.Formatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DailyQueueListReportService extends BirtReportServiceConfig {

    private static final Logger logger = LogManager.getLogger(DailyQueueListReportService.class);

    public List<DailyQueueListReport> getDailyQueueListReport(List<String> doctorIds, List<String> clinicIds, LocalDateTime startDate, LocalDateTime endDate) {

        List<DailyQueueListReport> reports = new ArrayList<>();

        logger.info("Report generating to doctorIds: " + doctorIds + " clinicIds: " + clinicIds + " start time: " + startDate + " end time:" + endDate);

        List<PatientVisitRegistry> visitRegistries = patientVisitRegistryDatabaseService
                .searchClinicAndDoctorsByStartTime(clinicIds, doctorIds, startDate, endDate);

        for (PatientVisitRegistry visitRegistry : visitRegistries) {

            DailyQueueListReport report = new DailyQueueListReport();
            report.setStartTime(Formatter.dateConvert(visitRegistry.getStartTime()));
            report.setEndTime(Formatter.dateConvert(visitRegistry.getEndTime()));

            Patient patient = patientDatabaseService.findPatientById(visitRegistry.getPatientId()).orElse(new Patient());
            report.feedPatientData(patient.getName(), patient.getPatientNumber(), patient.getUserId().getNumber());
            report.setPatientGender(patient.getGender().name());

            if (visitRegistry.getPreferredDoctorId() != null) {
                Doctor doctor = doctorDatabaseService.findOne(visitRegistry.getPreferredDoctorId()).orElse(new Doctor());
                report.feedDoctorData(doctor.getName());
            }

            Clinic clinic = clinicDatabaseService.findOne(visitRegistry.getClinicId()).orElse(new Clinic());
            report.feedClinicData(clinic.getClinicCode(), clinic.getName());

            Case aCase = caseDatabaseService.findByCaseId(visitRegistry.getCaseId());
            if (aCase == null) continue;
            SalesOrder salesOrder = aCase.getSalesOrder();
            if (salesOrder == null) continue;
            List<DailyQueueListItem> queueListItems = new ArrayList<>();
            int payableAmount = 0;
            for (Invoice invoice : salesOrder.getInvoices()) {
                if (invoice.getInvoiceState().equals(InvoiceState.DELETED)) continue;
                if (visitRegistry.getId().equals(invoice.getVisitId())) {
                    if (invoice.getInvoiceType().equals(InvoiceType.DIRECT)) {
                        for (PaymentInfo paymentInfo : invoice.getPaymentInfos()) {
                            DailyQueueListItem dailyQueueListItem = new DailyQueueListItem();
                            dailyQueueListItem.setBillMode(InvoiceType.DIRECT + ":" + paymentInfo.getBillMode().name());
                            dailyQueueListItem.setTotalPatientPaid(Formatter.currencyDecimalFormat(paymentInfo.getAmount()));
                            queueListItems.add(dailyQueueListItem);
                        }
                    } else {
                        Claim claim = invoice.getClaim();
                        if (claim == null) continue;
                        DailyQueueListItem dailyQueueListItem = new DailyQueueListItem();
                        CoveragePlan coveragePlan = medicalCoverageDatabaseService.findPlan(invoice.getPlanId());
                        dailyQueueListItem.setBillMode(InvoiceType.CREDIT + ":" + (coveragePlan == null ? "" : coveragePlan.getName()));
                        dailyQueueListItem.setTotalPatientPaid(Formatter.currencyDecimalFormat(claim.getClaimExpectedAmt()));
                        queueListItems.add(dailyQueueListItem);
                    }
                }
                payableAmount += invoice.getPayableAmount();
            }
            report.setTotalBillAmount(Formatter.currencyDecimalFormat(payableAmount));
            report.setItemList(queueListItems);
            reports.add(report);
        }
        return reports;
    }

    public List<DailyQueueListReport> getDailyQueueListReportData() {
        List<DailyQueueListReport> list = new ArrayList<>();

        List<DailyQueueListItem> list1 = new ArrayList<>();
        DailyQueueListItem item1 = new DailyQueueListItem(180F, "CACHE");
        DailyQueueListItem item2 = new DailyQueueListItem(800F, "CHEQUE");
        DailyQueueListItem item3 = new DailyQueueListItem(900F, "CHEQUE");
        list1.add(item1);
        list1.add(item2);
        list1.add(item3);

        DailyQueueListReport report1 = new DailyQueueListReport(new Date(), new Date(),
                "ClinicCode", "ClinicName", "PatientNumber", "PatientIdentity", "PatientName", "PatientGender"
                , "DoctorName", 780F, list1);
        DailyQueueListReport report2 = new DailyQueueListReport(new Date(), new Date(),
                "ClinicCode2", "ClinicName2", "PatientNumber2", "PatientIdentity2", "PatientName2", "PatientGender2"
                , "DoctorName2", 780F, list1);

        list.add(report1);
        list.add(report2);
        return list;
    }
}
