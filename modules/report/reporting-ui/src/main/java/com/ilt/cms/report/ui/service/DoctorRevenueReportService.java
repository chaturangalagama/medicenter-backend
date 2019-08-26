package com.ilt.cms.report.ui.service;

import com.ilt.cms.core.entity.casem.Case;
import com.ilt.cms.core.entity.casem.SalesItem;
import com.ilt.cms.core.entity.casem.SalesOrder;
import com.ilt.cms.core.entity.doctor.Doctor;
import com.ilt.cms.core.entity.item.Item;
import com.ilt.cms.core.entity.visit.PatientVisitRegistry;
import com.ilt.cms.report.ui.models.DoctorRevenueReport;
import com.ilt.cms.report.ui.util.Formatter;
import org.apache.commons.lang.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DoctorRevenueReportService extends BirtReportServiceConfig {

    private static final Logger logger = LogManager.getLogger(DoctorRevenueReportService.class);

    public List<DoctorRevenueReport> getDoctorRevenueReportData(List<String> doctorIds, LocalDateTime startDate, LocalDateTime endDate) {

        logger.info("Report generating to doctorIds: " + doctorIds + " start time: " + startDate + " end time:" + endDate);

        List<DoctorRevenueReport> reports = new ArrayList<>();
        List<PatientVisitRegistry> patientVisitRegistry =
                patientVisitRegistryDatabaseService.searchClinicAndDoctorsByStartTime(null, doctorIds, startDate, endDate);

        for (PatientVisitRegistry visitRegistry : patientVisitRegistry) {
            DoctorRevenueReport report = new DoctorRevenueReport();

            if (visitRegistry.getCaseId() != null) {
                Case byCaseId = caseDatabaseService.findByCaseId(visitRegistry.getCaseId());
                if (byCaseId == null) continue;
                if (visitRegistry.getPreferredDoctorId() != null) {
                    Doctor doctor = doctorDatabaseService.findOne(visitRegistry.getPreferredDoctorId()).orElse(new Doctor());
                    report.feedDoctorData(doctor.getName());
                    report.setStartTime(Formatter.dateConvert(visitRegistry.getStartTime()));
                }
                SalesOrder salesOrder = byCaseId.getSalesOrder();
                for (SalesItem purchaseItem : salesOrder.getPurchaseItems()) {
                    DoctorRevenueReport clone = (DoctorRevenueReport) SerializationUtils.clone(report);
                    clone.setQuantity((long) purchaseItem.getPurchaseQty());
                    clone.setTotalAmount(Formatter.currencyDecimalFormat(purchaseItem.getSoldPrice()));
                    clone.setTotalVisits((long) 1);
                    Item itemByItemId = itemDatabaseService.findItemByItemId(purchaseItem.getItemRefId()).orElse(new Item());
                    clone.feedItemData(itemByItemId.getName(), itemByItemId.getCode(), itemByItemId.getCategory());
                    reports.add(clone);
                }
            }
        }
        return reports;
    }
}
