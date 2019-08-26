package com.ilt.cms.report.ui.service;

import com.ilt.cms.report.ui.models.ReportOnTreatmentReport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportOnTreatmentReportService extends BirtReportServiceConfig {

    public List<ReportOnTreatmentReport> getReportData() {

        List<ReportOnTreatmentReport> list = new ArrayList<>();

        ReportOnTreatmentReport report1 = new ReportOnTreatmentReport("DoctorName",
                new Date(), "PatientName",
                "PatientNumber", 890F,
                "ItemCategoryCode1", "ItemCode", "ItemName",
                "UOM", "50");
        ReportOnTreatmentReport report2 = new ReportOnTreatmentReport("DoctorName2",
                new Date(), "PatientName2",
                "PatientNumber2", 450F,
                "ItemCategoryCode1", "ItemCode2", "ItemName2",
                "UOM2", "500");
        ReportOnTreatmentReport report3 = new ReportOnTreatmentReport("DoctorName",
                new Date(), "PatientName",
                "PatientNumber", 110F,
                "ItemCategoryCode2", "ItemCode", "ItemName",
                "UOM", "50");
        list.add(report1);
        list.add(report2);
        list.add(report3);
        return list;

    }
}
