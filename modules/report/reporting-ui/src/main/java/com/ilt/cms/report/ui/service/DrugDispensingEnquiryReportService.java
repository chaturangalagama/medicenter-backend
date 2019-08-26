package com.ilt.cms.report.ui.service;

import com.ilt.cms.core.entity.doctor.Doctor;
import com.ilt.cms.core.entity.item.Item;
import com.ilt.cms.core.entity.medical.DispatchItem;
import com.ilt.cms.core.entity.patient.Patient;
import com.ilt.cms.core.entity.visit.PatientVisitRegistry;
import com.ilt.cms.report.ui.models.DrugDispensingEnquiryReport;
import com.ilt.cms.report.ui.util.Formatter;
import org.apache.commons.lang.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DrugDispensingEnquiryReportService extends BirtReportServiceConfig {

    private static final Logger logger = LogManager.getLogger(DrugDispensingEnquiryReportService.class);

    public List<DrugDispensingEnquiryReport> getPatientDrugDispenseData(List<String> doctorIds, LocalDateTime startDate, LocalDateTime endDate, String drugCode) {

        logger.info("Report generating to doctorIds: " + doctorIds +
                " start time: " + startDate + " end time:" + endDate + " drug code: " + drugCode);

        List<DrugDispensingEnquiryReport> reports = new ArrayList<>();

        List<PatientVisitRegistry> visitRegistries = patientVisitRegistryDatabaseService
                .searchClinicAndDoctorsByStartTime(null, doctorIds, startDate, endDate);

        Item item = itemDatabaseService.findItemByCode(drugCode).orElse(new Item());

        for (PatientVisitRegistry visitRegistry : visitRegistries) {
            DrugDispensingEnquiryReport report = new DrugDispensingEnquiryReport();
            report.setStartTime(Formatter.dateConvert(visitRegistry.getStartTime()));
            loadPatientData(visitRegistry.getPatientId(), report);
            loadDoctorData(visitRegistry.getPreferredDoctorId(), report);
            for (DispatchItem dispatchItem : visitRegistry.getMedicalReference().getDispatchItems()) {
                if (drugCode == null || drugCode.equals("")) {
                    DrugDispensingEnquiryReport clone = (DrugDispensingEnquiryReport) SerializationUtils.clone(report);
                    clone.setQuantity((float) dispatchItem.getQuantity());
                    clone.setUom(dispatchItem.getDosageUom());
                    reports.add(clone);
                } else if (drugCode.equals(item.getCode())) {
                    DrugDispensingEnquiryReport clone = (DrugDispensingEnquiryReport) SerializationUtils.clone(report);
                    clone.setQuantity((float) dispatchItem.getQuantity());
                    clone.setUom(dispatchItem.getDosageUom());
                    reports.add(clone);
                }
            }
        }

        logger.info("Number of data rows found in report: " + reports.size());
        return reports;
    }

    public void loadDoctorData(String doctorId, DrugDispensingEnquiryReport report) {
        if (doctorId != null) {
            Doctor doctor = doctorDatabaseService.findOne(doctorId).orElse(new Doctor());
            report.feedDoctorData(doctor.getName());
        }
    }

    public void loadPatientData(String patientId, DrugDispensingEnquiryReport report) {
        if (patientId != null) {
            Patient patient = patientDatabaseService.findPatientById(patientId).orElse(new Patient());
            report.feedPatientData(patient.getName(), patient.getPatientNumber(), patient.getUserId().getNumber());
            report.setPatientContactNumber(patient.getContactNumber().getNumber());
            report.setPatientGender(String.valueOf(patient.getGender()));
            report.setPatientAddress(patient.getAddress().getAddress() +
                    patient.getAddress().getCountry() +
                    patient.getAddress().getPostalCode());
            report.setPatientAge(Period.between(patient.getDob(), LocalDate.now()).getYears());
        }
    }

    public List<DrugDispensingEnquiryReport> getReportDetails() {
        List<DrugDispensingEnquiryReport> list = new ArrayList<>();

        //Calculate the age using the DOB and current date. Pass int value to the report.

        DrugDispensingEnquiryReport report1 = new DrugDispensingEnquiryReport(new Date(), "DoctorName",
                "PatientName", 28, "Male", "PatientContactNumber",
                "PatientNumber", 28F, "UOM");
        DrugDispensingEnquiryReport report2 = new DrugDispensingEnquiryReport(new Date(), "DoctorName",
                "PatientName2", 40, "Male", "PatientContactNumber2",
                "PatientNumber2", 28F, "UOM");

        list.add(report1);
        list.add(report2);

        return list;
    }
}
