package com.ilt.cms.report.ui.service;

import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.core.entity.casem.Case;
import com.ilt.cms.core.entity.casem.Claim;
import com.ilt.cms.core.entity.casem.Invoice;
import com.ilt.cms.core.entity.casem.Invoice.InvoiceState;
import com.ilt.cms.core.entity.casem.Invoice.InvoiceType;
import com.ilt.cms.core.entity.casem.SalesOrder;
import com.ilt.cms.core.entity.visit.PatientVisitRegistry;
import com.ilt.cms.report.ui.models.PatientTreatmentRecordsReport;
import com.ilt.cms.report.ui.models.WeeklyRevenueSummaryReport;
import com.ilt.cms.report.ui.util.Formatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Map.Entry;

public class WeeklyRevenueSummaryReportService extends BirtReportServiceConfig {

    private static final Logger logger = LogManager.getLogger(PatientTreatmentRecordsReport.class);

    public List<WeeklyRevenueSummaryReport> getPatientTreatmentRecord(List<String> clinicIds, LocalDateTime startDate, LocalDateTime endDate) {

        logger.info("Report generating to clinicIds: " + clinicIds + " start time: " + startDate + " end time:" + endDate);

        List<WeeklyRevenueSummaryReport> reports = new ArrayList<>();

        List<PatientVisitRegistry> visitRegistries = patientVisitRegistryDatabaseService
                .searchClinicAndDoctorsByStartTime(clinicIds, null, startDate, endDate);

        Map<LocalDate, Map<String, DataCollection>> localDateMapMap = new HashMap<>();

        for (PatientVisitRegistry visitRegistry : visitRegistries) {
            WeeklyRevenueSummaryReport report = new WeeklyRevenueSummaryReport();

            LocalDate key = visitRegistry.getStartTime().toLocalDate();

            if (!localDateMapMap.containsKey(key)) {
                localDateMapMap.put(key, new HashMap<>());
            }

            if (localDateMapMap.containsKey(key)) {
                if (localDateMapMap.get(key).containsKey(visitRegistry.getClinicId())) {
                    DataCollection dataCollection = localDateMapMap.get(key).get(visitRegistry.getClinicId());
                    loadData(visitRegistry, dataCollection);
                } else {
                    DataCollection dataCollection = new DataCollection();
                    loadData(visitRegistry, dataCollection);
                    localDateMapMap.get(key).put(visitRegistry.getClinicId(), dataCollection);
                }
            }
        }

        for (Entry<LocalDate, Map<String, DataCollection>> localDateMapEntry : localDateMapMap.entrySet()) {
            for (Map<String, DataCollection> value : localDateMapMap.values()) {
                for (Entry<String, DataCollection> data : value.entrySet()) {
                    WeeklyRevenueSummaryReport report = new WeeklyRevenueSummaryReport();
                    report.setStartTime(Formatter.dateConvert(localDateMapEntry.getKey()));
                    Clinic clinic = clinicDatabaseService.findOne(data.getKey()).orElse(new Clinic());
                    report.setVisits((float) data.getValue().getVisitCount());
                    report.feedClinicData(clinic.getClinicCode(), clinic.getName());
                    report.setPatientPayable(Formatter.currencyDecimalFormat(data.getValue().getPatientPayable()));
                    report.setInsurancePayable(Formatter.currencyDecimalFormat(data.getValue().getInsurancePayable()));
                    report.setMediSavePayable(Formatter.currencyDecimalFormat(data.getValue().getMediSavePayable()));
                    report.setCorporatePayable(Formatter.currencyDecimalFormat(data.getValue().getCooperatePayable()));
                    report.setChasPayable(Formatter.currencyDecimalFormat(data.getValue().getChasPayable()));
                }
            }
        }
        logger.info("Number of data rows found in report: " + reports.size());
        return reports;
    }

    private void loadData(PatientVisitRegistry visitRegistry, DataCollection dataCollection) {
        dataCollection.addVisitCount(1);
        Case aCase = caseDatabaseService.findByCaseId(visitRegistry.getCaseId());
        if (aCase != null) {
            SalesOrder salesOrder = aCase.getSalesOrder();
            for (Invoice invoice : salesOrder.getInvoices()) {
                if (invoice.getInvoiceState().equals(InvoiceState.DELETED)) continue;
                if (visitRegistry.getId().equals(invoice.getVisitId())) {
                    if (invoice.getInvoiceType().equals(InvoiceType.DIRECT)) {
                        dataCollection.addPatientPayable(invoice.getPayableAmount());
                    } else {
                        Claim claim = invoice.getClaim();
                        if (claim == null) continue;
                        dataCollection.addInsurancePayable(claim.getClaimExpectedAmt());
                    }
                }
            }
        }
    }

    public List<WeeklyRevenueSummaryReport> getWeeklyRevenueSummaryReportData(List<String> clinicIdsString, LocalDate startDate, LocalDate endDate) {
        List<WeeklyRevenueSummaryReport> list = new ArrayList<>();

        //Report is grouped by date and added a separate date to check report grouping
        Date date = new GregorianCalendar(2019, Calendar.FEBRUARY, 5).getTime();

        WeeklyRevenueSummaryReport report1 = new WeeklyRevenueSummaryReport("ClinicCode1",
                new Date(), 8F, 120F, 200F, 75F,
                25F, 10F);
        WeeklyRevenueSummaryReport report2 = new WeeklyRevenueSummaryReport("ClinicCode2",
                new Date(), 8F, 120F, 200F, 75F,
                25F, 10F);

        WeeklyRevenueSummaryReport report3 = new WeeklyRevenueSummaryReport("ClinicCode2",
                date, 8F, 120F, 100F, 89F,
                2F, 10F);
        WeeklyRevenueSummaryReport report4 = new WeeklyRevenueSummaryReport("ClinicCode2",
                date, 8F, 120F, 300F, 79F,
                95F, 10F);
        WeeklyRevenueSummaryReport report5 = new WeeklyRevenueSummaryReport("ClinicCode2",
                date, 8F, 120F, 20F, 55F,
                26F, 10F);

        list.add(report1);
        list.add(report2);
        list.add(report3);
        list.add(report4);
        list.add(report5);

        return list;
    }

    private static class DataCollection {

        private int visitCount;
        private int patientPayable;
        private int cooperatePayable;
        private int insurancePayable;
        private int chasPayable;
        private int mediSavePayable;

        public int getVisitCount() {
            return visitCount;
        }

        public void addVisitCount(int visitCount) {
            this.visitCount += visitCount;
        }

        int getPatientPayable() {
            return patientPayable;
        }

        void addPatientPayable(int patientPayable) {
            this.patientPayable += patientPayable;
        }

        int getCooperatePayable() {
            return cooperatePayable;
        }

        public void addCooperatePayable(int cooperatePayable) {
            this.cooperatePayable += cooperatePayable;
        }

        int getInsurancePayable() {
            return insurancePayable;
        }

        void addInsurancePayable(int insurancePayable) {
            this.insurancePayable += insurancePayable;
        }

        int getChasPayable() {
            return chasPayable;
        }

        public void addChasPayable(int chasPayable) {
            this.chasPayable += chasPayable;
        }

        int getMediSavePayable() {
            return mediSavePayable;
        }

        public void addMediSavePayable(int mediSavePayable) {
            this.mediSavePayable += mediSavePayable;
        }
    }
}
