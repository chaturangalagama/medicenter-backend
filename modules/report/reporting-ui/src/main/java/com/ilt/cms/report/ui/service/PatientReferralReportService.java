package com.ilt.cms.report.ui.service;

import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.core.entity.casem.Case;
import com.ilt.cms.core.entity.consultation.PatientReferral;
import com.ilt.cms.core.entity.consultation.PatientReferral.PatientReferralDetails;
import com.ilt.cms.core.entity.doctor.Doctor;
import com.ilt.cms.core.entity.medical.MedicalReference;
import com.ilt.cms.core.entity.patient.Patient;
import com.ilt.cms.core.entity.visit.PatientVisitRegistry;
import com.ilt.cms.report.ui.models.PatientReferralReport;
import com.ilt.cms.report.ui.util.Formatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PatientReferralReportService extends BirtReportServiceConfig {

    private static final Logger logger = LogManager.getLogger(PatientReferralReportService.class);

    public List<PatientReferralReport> getPatientReferralReport(List<String> doctorIds, LocalDateTime startDate, LocalDateTime endDate) {

        logger.info("Report generating to doctorIds: " + doctorIds + " start time: " + startDate + " end time:" + endDate);

        List<PatientReferralReport> list = new ArrayList<>();

        List<PatientVisitRegistry> visitRegistries = patientVisitRegistryDatabaseService
                .searchClinicAndDoctorsByStartTime(null, doctorIds, startDate, endDate);

        for (PatientVisitRegistry visitRegistry : visitRegistries) {

            MedicalReference medicalReference = visitRegistry.getMedicalReference();
            if (medicalReference == null) continue;
            PatientReferral patientReferral = medicalReference.getPatientReferral();
            if (patientReferral == null) continue;
            for (PatientReferralDetails referral : patientReferral.getPatientReferrals()) {
                PatientReferralReport report = new PatientReferralReport();
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

                report.setVisitNumber(visitRegistry.getVisitNumber());
                report.setStartTime(Formatter.dateConvert(visitRegistry.getStartTime()));
                report.setAttendingDoctor(referral.getExternalReferralDetails().getDoctorName());
                report.setContactNumber(referral.getExternalReferralDetails().getPhoneNumber());
                clinic = clinicDatabaseService.findOne(visitRegistry.getClinicId()).orElse(new Clinic());
                report.setRefClinicCode(clinic.getClinicCode());
                report.setReason(referral.getMemo());
                list.add(report);
            }
        }

        logger.info("Number of data rows found in report: " + list.size());
        return list;
    }
}
