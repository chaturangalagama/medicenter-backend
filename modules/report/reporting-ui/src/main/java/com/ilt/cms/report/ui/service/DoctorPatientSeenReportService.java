package com.ilt.cms.report.ui.service;

import com.ilt.cms.core.entity.casem.Case;
import com.ilt.cms.core.entity.doctor.Doctor;
import com.ilt.cms.core.entity.patient.Patient;
import com.ilt.cms.core.entity.visit.PatientVisitRegistry;
import com.ilt.cms.report.ui.models.DoctorPatientSeenReport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DoctorPatientSeenReportService extends BirtReportServiceConfig {

    private static final Logger logger = LogManager.getLogger(DoctorPatientSeenReportService.class);

    public DoctorPatientSeenReportService() {
    }

    public List<DoctorPatientSeenReport> getDoctorPatientSeenReport(String[] doctorIds, LocalDate startDate, LocalDate endDate) {

        List<DoctorPatientSeenReport> doctorPatientSeenReports = new ArrayList<>();
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atStartOfDay();
        int i = 0;

        for (int x = 0; x < doctorIds.length; x++) {
            String doctorId = doctorIds[x].replace("\"", "").replace(" ", "");
            Doctor doctor = doctorDatabaseService.findOne(doctorId).get();
            List<PatientVisitRegistry> list =
                    patientVisitRegistryDatabaseService.findByPreferredDoctorIdAndStartTime(doctorId, start, end);
            if (list != null) {
                for (PatientVisitRegistry registry : list) {
                    Patient patient = patientDatabaseService.findPatientById(registry.getPatientId()).get();
                    Case relatedCase = new Case();
                    if (registry.getCaseId() != null && !registry.getCaseId().equals("")) {
                        relatedCase = caseDatabaseService.findByCaseId(registry.getCaseId());
                    }
                    if (patient != null) {
                        i++;
                        doctorPatientSeenReports.add(new DoctorPatientSeenReport(i, patient.getPatientNumber(),
                                registry.getVisitNumber(), registry.getStartTime(), patient.getName(),
                                patient.getUserId().getNumber(), patient.getAddress().getAddress(), doctor.getName(), relatedCase.getCaseNumber()));
                    }
                }
            }
        }
        return doctorPatientSeenReports;
    }
}
