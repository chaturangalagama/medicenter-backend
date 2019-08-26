package com.ilt.cms.report.ui.service;

import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.core.entity.casem.Case;
import com.ilt.cms.core.entity.doctor.Doctor;
import com.ilt.cms.core.entity.patient.Patient;
import com.ilt.cms.core.entity.visit.PatientVisitRegistry;
import com.ilt.cms.report.ui.models.IocPatientReport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class IocPatientReportService extends BirtReportServiceConfig {

    private static final Logger logger = LogManager.getLogger(IocPatientReportService.class);

    public List<IocPatientReport> getIocPatientReport(List<String> clinicIds, LocalDateTime startDate, LocalDateTime endDate) {

        logger.info("Report generating to clinicIds: " + clinicIds + " start time: " + startDate + " end time:" + endDate);

        List<IocPatientReport> list = new ArrayList<>();
        List<PatientVisitRegistry> visitRegistries = patientVisitRegistryDatabaseService
                .findByClinicIdsAndStartEndTimeBetween(clinicIds, startDate, endDate);

        for (PatientVisitRegistry visitRegistry : visitRegistries) {
            IocPatientReport report = new IocPatientReport();
            Clinic clinic = clinicDatabaseService.findOne(visitRegistry.getClinicId()).orElse(new Clinic());
            report.feedClinicData(clinic.getClinicCode(), clinic.getName());
            if (visitRegistry.getCaseId() != null) {
                Case byCaseId = caseDatabaseService.findByCaseId(visitRegistry.getCaseId());
                report.feedCaseData(byCaseId.getCaseNumber());
            }
            Patient patient = patientDatabaseService.findPatientById(visitRegistry.getPatientId()).orElse(new Patient());
            report.feedPatientData(patient.getName(), patient.getPatientNumber(), patient.getUserId().getNumber());
            if (visitRegistry.getPreferredDoctorId() != null) {
                Doctor doctor = doctorDatabaseService.findOne(visitRegistry.getPreferredDoctorId()).orElse(new Doctor());
                report.feedDoctorData(doctor.getName());
            }
            list.add(report);
        }
        logger.info("Number of data rows found in report: " + list.size());
        return list;
    }
}
