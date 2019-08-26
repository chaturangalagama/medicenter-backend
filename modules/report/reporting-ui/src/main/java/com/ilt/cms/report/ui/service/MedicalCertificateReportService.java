package com.ilt.cms.report.ui.service;

import com.ilt.cms.core.entity.consultation.MedicalCertificate;
import com.ilt.cms.core.entity.doctor.Doctor;
import com.ilt.cms.core.entity.medical.MedicalReference;
import com.ilt.cms.core.entity.patient.Patient;
import com.ilt.cms.core.entity.visit.PatientVisitRegistry;
import com.ilt.cms.report.ui.models.MedicalCertificateReport;
import com.ilt.cms.report.ui.util.Formatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MedicalCertificateReportService extends BirtReportServiceConfig {

    private static final Logger logger = LogManager.getLogger(MedicalCertificateReportService.class);

    public List<MedicalCertificateReport> getMedicalCertificateReport(List<String> doctorIds, List<String> clinicIds, LocalDateTime startDate, LocalDateTime endDate) {

        logger.info("Report generating to doctorIds: " + doctorIds + " clinicIds: " + clinicIds + " start time: " + startDate + " end time:" + endDate);

        List<MedicalCertificateReport> reports = new ArrayList<>();

        List<PatientVisitRegistry> visitRegistries = patientVisitRegistryDatabaseService
                .searchClinicAndDoctorsByStartTime(clinicIds, doctorIds, startDate, endDate);

        for (PatientVisitRegistry visitRegistry : visitRegistries) {

            MedicalReference medicalReference = visitRegistry.getMedicalReference();
            if (medicalReference == null) continue;
            for (MedicalCertificate medicalCertificate : medicalReference.getMedicalCertificates()) {
                MedicalCertificateReport report = new MedicalCertificateReport();
                Patient patient = patientDatabaseService.findPatientById(visitRegistry.getPatientId()).orElse(new Patient());
                report.feedPatientData(patient.getName(), patient.getPatientNumber(), patient.getUserId().getNumber());
                if (visitRegistry.getPreferredDoctorId() != null) {
                    Doctor doctor = doctorDatabaseService.findOne(visitRegistry.getPreferredDoctorId()).orElse(new Doctor());
                    report.feedDoctorData(doctor.getName());
                }
                report.setStartTime(Formatter.dateConvert(visitRegistry.getStartTime()));
                report.setFromDate(Formatter.dateConvert(medicalCertificate.getStartDate()));
                report.setToDate(Formatter.dateConvert(visitRegistry.getEndTime()));
                report.setNumberOfDays(medicalCertificate.getNumberOfDays());
                report.setPurpose(medicalCertificate.getPurpose());
                report.setHalfDay(medicalCertificate.getHalfDayOption().name());
                reports.add(report);
            }
        }
        logger.info("Number of data rows found in report: " + reports.size());
        return reports;
    }
}
