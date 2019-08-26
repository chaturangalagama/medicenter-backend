
package com.ilt.cms.report.ui.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.database.clinic.ClinicDatabaseService;
import com.ilt.cms.report.ui.builder.HttpRequestEncoder;
import com.ilt.cms.report.ui.builder.TransactionQueryBuilder;
import com.ilt.cms.report.ui.domain.TransactionForm;
import com.ilt.cms.report.ui.report.BirtReportConfiguration;
import com.ilt.cms.report.ui.report.DynamicTransactionReportDesign;
import com.ilt.cms.report.ui.report.ReportDesignOverrider;
import com.ilt.cms.report.ui.util.BartTemplateNameUtil;
import com.ilt.cms.repository.spring.ClinicRepository;
import com.ilt.cms.repository.spring.DoctorRepository;
import com.lippo.cms.container.CmsServiceResponse;
import com.lippo.commons.util.StatusCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Controller
//@RolesAllowed("ROLE_AA_ADMIN")
public class TransactionController {

    private static final Logger logger = LogManager.getLogger(TransactionController.class);
    private static final List<String> CLINIC_BYPASS_ROLES = Collections.singletonList("ROLE_ALL_CLINIC_REPORTS");
    private static final List<String> DOCTOR_BYPASS_ROLES = Collections.singletonList("ROLE_ALL_DOCTOR_REPORTS");

    private static final String DATE_TIME_PATTERN = "uuuu-MM-dd HH:mm";

    @Autowired
    private ClinicRepository clinicRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private BirtReportConfiguration birtReportConfiguration;
    @Autowired
    private ClinicDatabaseService clinicDatabaseService;

    @RequestMapping(value = "transaction-report", method = RequestMethod.GET)
    public String showTransactionSearchPage(Map<String, Object> map) {
        TransactionForm form = new TransactionForm();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime today = now.minusMinutes(now.getMinute());
        form.setStartDate(today.minusDays(1).format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)));
        form.setEndDate(today.format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)));
        map.put("transactionForm", form);
        return "transaction";
    }

    @RequestMapping(value = "test-report")
    public String testReport(@ModelAttribute("transactionForm") TransactionForm form, BindingResult result, HttpServletRequest request) {
        return new HttpRequestEncoder("redirect:frameset").param("__report", BartTemplateNameUtil.TEST_REPORT).toString();
    }

    @RequestMapping("/daily_queue_list")
    public String dailyQueueListReport(Principal principal, HttpServletRequest request) throws Exception {

        System.out.println(request.getParameter("body"));

        HttpSession session = request.getSession(true);
        session.setAttribute("test11", "test");
        ReportDesignOverrider designOverrider = new ReportDesignOverrider(birtReportConfiguration);
        designOverrider.generateReport(request, null, "2_daily_queue_list_cmsdua.rptdesign");

        ObjectMapper mapper = new ObjectMapper();

        TypeReference<HashMap<String, String>> typeRef = new TypeReference() {
        };
        Map<String, String> bodyMap = mapper.readValue(request.getParameter("body"), typeRef);


        String startDate = bodyMap.get("startDate");
        String endDate = bodyMap.get("endDate");
        if (startDate == null || endDate == null) {
            throw new Exception("Invalid Parameter");
        }

        String isDetailString = bodyMap.get("isDetail") == null ? "true" : bodyMap.get("isDetail");
        boolean isDetail = "true".equalsIgnoreCase(isDetailString);

        String finalClinicIdsString = verifyClinicRoles(principal, bodyMap);

        String finalDoctorIdsString = verifyDoctorRoles(principal, bodyMap, false);

        return new HttpRequestEncoder("redirect:frameset")
                .param("__report", "2_daily_queue_list_cmsdua.rptdesign")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("clinicIds", finalClinicIdsString)
                .param("doctorIds", finalDoctorIdsString)
                .param("isDetail", isDetail).toString();
    }

    @RequestMapping("/daily_patient_register_report")
    public String dailyPatientRegisterReport(Principal principal, HttpServletRequest request) throws Exception {

        System.out.println(request.getParameter("body"));

        HttpSession session = request.getSession(true);
        session.setAttribute("test11", "test");
        ReportDesignOverrider designOverrider = new ReportDesignOverrider(birtReportConfiguration);
        designOverrider.generateReport(request, null, "3_daily_patient_register_report.rptdesign");

        ObjectMapper mapper = new ObjectMapper();

        TypeReference<HashMap<String, String>> typeRef = new TypeReference() {
        };
        Map<String, String> bodyMap = mapper.readValue(request.getParameter("body"), typeRef);


        String startDate = bodyMap.get("startDate");
        String endDate = bodyMap.get("endDate");
        if (startDate == null || endDate == null) {
            throw new Exception("Invalid Parameter");
        }

        String finalClinicIdsString = verifyClinicRoles(principal, bodyMap);

        String finalDoctorIdsString = verifyDoctorRoles(principal, bodyMap, false);


        return new HttpRequestEncoder("redirect:frameset")
                .param("__report", "3_daily_patient_register_report.rptdesign")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("clinicIds", finalClinicIdsString)
                .param("doctorIds", finalDoctorIdsString).toString();
    }


    @RequestMapping("/drug_dispensing_enquiry")
    public String drugDispensingEnquiry(Principal principal, HttpServletRequest request) throws Exception {
        ReportDesignOverrider designOverrider = new ReportDesignOverrider(birtReportConfiguration);
        designOverrider.generateReport(request, null, "6_drug_dispensing_enquiry_cmsdua.rptdesign");

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef = new TypeReference() {
        };
        Map<String, String> bodyMap = mapper.readValue(request.getParameter("body"), typeRef);

        String startDate = bodyMap.get("startDate");
        String endDate = bodyMap.get("endDate");
        String drugCode = bodyMap.get("drugCode");
        if (startDate == null || endDate == null || drugCode == null) {
            throw new Exception("Invalid Parameter");
        }


        String finalDoctorIdsString = verifyDoctorRoles(principal, bodyMap, true);

        return new HttpRequestEncoder("redirect:frameset")
                .param("__report", "6_drug_dispensing_enquiry_cmsdua.rptdesign")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("drugCode", drugCode)
                .param("doctorIds", finalDoctorIdsString)
                .toString();
    }


    @RequestMapping("/patient_treatment_records")
    public String patientTreatmentReecords(Principal principal, HttpServletRequest request) throws Exception {
        ReportDesignOverrider designOverrider = new ReportDesignOverrider(birtReportConfiguration);
        designOverrider.generateReport(request, null, "7_patient_treatment_records_cmsdua.rptdesign");

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef = new TypeReference() {
        };
        Map<String, String> bodyMap = mapper.readValue(request.getParameter("body"), typeRef);

        String startDate = bodyMap.get("startDate");
        String endDate = bodyMap.get("endDate");
        if (startDate == null || endDate == null) {
            throw new Exception("Invalid Parameter");
        }

        String finalClinicIdsString = verifyClinicRoles(principal, bodyMap);

        String finalDoctorIdsString = verifyDoctorRoles(principal, bodyMap, false);

        String itemCodesString = bodyMap.get("itemCodes");
        String[] itemCodes = itemCodesString == null ? new String[0] : itemCodesString.split(",");
        String finalItemCodesString = formatBirtArray(itemCodes);

        String itemCategoryCodesString = bodyMap.get("itemCategoryCodes");
        String[] itemCategoryCodes = itemCategoryCodesString == null ? new String[0] : itemCategoryCodesString.split(",");
        String finalItemCategoryCodesString = formatBirtArray(itemCategoryCodes);

        String financialPlanIdString = bodyMap.get("itemCateGoryCodes");
        String[] financialPlanId = financialPlanIdString == null ? new String[0] : financialPlanIdString.split(",");
        String finalFinanacialPlanString = formatBirtArray(financialPlanId);

        return new HttpRequestEncoder("redirect:frameset")
                .param("__report", "7_patient_treatment_records_cmsdua.rptdesign")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("doctorIds", finalDoctorIdsString)
                .param("clinicIds", finalClinicIdsString)
                .param("itemCodes", finalItemCodesString)
                .param("itemCategoryCodes", finalItemCategoryCodesString)
                .param("financialPlanId", finalFinanacialPlanString)
                .toString();
    }


    @RequestMapping("/medical_certificate_report")
    public String patientTreatmentRecords(Principal principal, HttpServletRequest request) throws Exception {

        ReportDesignOverrider designOverrider = new ReportDesignOverrider(birtReportConfiguration);
        designOverrider.generateReport(request, null, "8_medical_certificate_report_cmsdua.rptdesign");

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef = new TypeReference() {
        };
        Map<String, String> bodyMap = mapper.readValue(request.getParameter("body"), typeRef);


        String startDate = bodyMap.get("startDate");
        String endDate = bodyMap.get("endDate");
        if (startDate == null || endDate == null) {
            throw new Exception("Invalid Parameter");
        }

        String finalClinicIdsString = verifyClinicRoles(principal, bodyMap);
        String finalDoctorIdsString = verifyDoctorRoles(principal, bodyMap, false);

        return new HttpRequestEncoder("redirect:frameset")
                .param("__report", "8_medical_certificate_report_cmsdua.rptdesign")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("doctorIds", finalDoctorIdsString)
                .param("clinicIds", finalClinicIdsString)
                .toString();
    }

    @RequestMapping("/patient_consultation_history_enquiry")
    public String patientConsultationHistory(HttpServletRequest request) throws Exception {
        ReportDesignOverrider designOverrider = new ReportDesignOverrider(birtReportConfiguration);
        designOverrider.generateReport(request, null, "10_patient_consultation_history_enquiry.rptdesign");

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef = new TypeReference() {
        };
        Map<String, String> bodyMap = mapper.readValue(request.getParameter("body"), typeRef);

        String patientId = bodyMap.get("patientId");
        if (patientId == null) {
            throw new Exception("Invalid Parameter");
        }
        return new HttpRequestEncoder("redirect:frameset")
                .param("__report", "10_patient_consultation_history_enquiry.rptdesign")
                .param("patientId", patientId).toString();
    }

    @RequestMapping("/pos_collection_summary")
    public String posCollectionSummary(Principal principal, HttpServletRequest request) throws Exception {
        ReportDesignOverrider designOverrider = new ReportDesignOverrider(birtReportConfiguration);
        designOverrider.generateReport(request, null, "21_pos_collection_summary.rptdesign");

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef = new TypeReference() {
        };
        Map<String, String> bodyMap = mapper.readValue(request.getParameter("body"), typeRef);

        String startDate = bodyMap.get("startDate");
        String endDate = bodyMap.get("endDate");
        if (startDate == null || endDate == null) {
            throw new Exception("Invalid Parameter");
        }

        String finalClinicIdsString = verifyClinicRoles(principal, bodyMap);

        return new HttpRequestEncoder("redirect:frameset")
                .param("__report", "21_pos_collection_summary.rptdesign")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("clinicIds", finalClinicIdsString)
                .toString();
    }

    @RequestMapping("/revenue_collection_report")
    public String revenueCollectionReport(Principal principal, HttpServletRequest request) throws Exception {
        ReportDesignOverrider designOverrider = new ReportDesignOverrider(birtReportConfiguration);
        designOverrider.generateReport(request, null, "24_revenue_collection_report.rptdesign");

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef = new TypeReference() {
        };
        Map<String, String> bodyMap = mapper.readValue(request.getParameter("body"), typeRef);

        String startDate = bodyMap.get("startDate");
        String endDate = bodyMap.get("endDate");
        if (startDate == null || endDate == null) {
            throw new Exception("Invalid Parameter");
        }

        String finalClinicIdsString = verifyClinicRoles(principal, bodyMap);


        return new HttpRequestEncoder("redirect:frameset")
                .param("__report", "24_revenue_collection_report.rptdesign")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("clinicIds", finalClinicIdsString)
                .toString();
    }


    @RequestMapping("/revenue_analysis_report")
    public String revenueAnalysisReport(Principal principal, HttpServletRequest request) throws Exception {
        logger.info("Request body" + request.getParameter("body"));
        ReportDesignOverrider designOverrider = new ReportDesignOverrider(birtReportConfiguration);
        designOverrider.generateReport(request, null, "25_revenue_analysis_report.rptdesign");

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef = new TypeReference() {
        };
        Map<String, String> bodyMap = mapper.readValue(request.getParameter("body"), typeRef);

        String startDate = bodyMap.get("startDate");
        String endDate = bodyMap.get("endDate");
        if (startDate == null || endDate == null) {
            throw new Exception("Invalid Parameter " + request.getParameter("body"));
        }

        String finalClinicIdsString = verifyClinicRoles(principal, bodyMap);

        return new HttpRequestEncoder("redirect:frameset")
                .param("__report", "25_revenue_analysis_report.rptdesign")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .toString();
    }


    @RequestMapping("/revenue_management_report")
    public String revenueManagementReport(Principal principal, HttpServletRequest request) throws Exception {
        ReportDesignOverrider designOverrider = new ReportDesignOverrider(birtReportConfiguration);
        designOverrider.generateReport(request, null, "26_revenue_management_report.rptdesign");

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef = new TypeReference() {
        };
        Map<String, String> bodyMap = mapper.readValue(request.getParameter("body"), typeRef);

        String startDate = bodyMap.get("startDate");
        String endDate = bodyMap.get("endDate");
        if (startDate == null || endDate == null) {
            throw new Exception("Invalid Parameter " + request.getParameter("body"));
        }

        String finalClinicIdsString = verifyClinicRoles(principal, bodyMap);

        return new HttpRequestEncoder("redirect:frameset")
                .param("__report", "26_revenue_management_report.rptdesign")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("clinicIds", finalClinicIdsString)
                .toString();
    }


    @RequestMapping("/private_corporate_patient_listing")
    public String privateCorporatePatientListing(Principal principal, HttpServletRequest request) throws Exception {
        ReportDesignOverrider designOverrider = new ReportDesignOverrider(birtReportConfiguration);
        designOverrider.generateReport(request, null, "27_private_corporate_patient_listing_cmsdua.rptdesign");

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef = new TypeReference() {
        };
        Map<String, String> bodyMap = mapper.readValue(request.getParameter("body"), typeRef);

        String startDate = bodyMap.get("startDate");
        String endDate = bodyMap.get("endDate");
        if (startDate == null || endDate == null) {
            throw new Exception("Invalid Parameter ");
        }

        String finalDoctorIdsString = verifyDoctorRoles(principal, bodyMap, false);

        String statusString = bodyMap.get("statuses");
        String[] status = statusString == null ? new String[0] : statusString.split(",");
        String finalStatusString = formatBirtArray(status);

        String coverageTypeString = bodyMap.get("coverageType");
        String[] coverageType = coverageTypeString == null ? new String[0] : coverageTypeString.split(",");
        String finalCoverageTypeString = formatBirtArray(coverageType);

        String finalPatientNric = "";
        if (bodyMap.get("patientNric") != null && !bodyMap.get("patientNric").equals("null")) {
            finalPatientNric = "\"" + bodyMap.get("patientNric") + "\"";
        }

        String finalClinicIdsString = verifyClinicRoles(principal, bodyMap);

        return new HttpRequestEncoder("redirect:frameset")
                .param("__report", "27_private_corporate_patient_listing_cmsdua.rptdesign")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("doctorIds", finalDoctorIdsString)
                .param("coverageType", finalCoverageTypeString)
                .param("statuses", finalStatusString)
                .param("clinicIds", finalClinicIdsString)
                .param("patientIdentityNumber", finalPatientNric)
                .toString();
    }


    @RequestMapping("/private_corporate_patient_listing_short")
    public String privateCorporatePatientListingShort(Principal principal, HttpServletRequest request) throws Exception {
        ReportDesignOverrider designOverrider = new ReportDesignOverrider(birtReportConfiguration);
        designOverrider.generateReport(request, null, "27_private_corporate_patient_listing_short_cmsdua.rptdesign");

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef = new TypeReference() {
        };
        Map<String, String> bodyMap = mapper.readValue(request.getParameter("body"), typeRef);

        String startDate = bodyMap.get("startDate");
        String endDate = bodyMap.get("endDate");
        if (startDate == null || endDate == null) {
            throw new Exception("Invalid Parameter ");
        }

        String finalDoctorIdsString = verifyDoctorRoles(principal, bodyMap, false);

        String statusString = bodyMap.get("statuses");
        String[] status = statusString == null ? new String[0] : statusString.split(",");
        String finalStatusString = formatBirtArray(status);

        String coverageTypeString = bodyMap.get("coverageType");
        String[] coverageType = coverageTypeString == null ? new String[0] : coverageTypeString.split(",");
        String finalCoverageTypeString = formatBirtArray(coverageType);

        String finalClinicIdsString = verifyClinicRoles(principal, bodyMap);

        return new HttpRequestEncoder("redirect:frameset")
                .param("__report", "27_private_corporate_patient_listing_short_cmsdua.rptdesign")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("doctorIds", finalDoctorIdsString)
                .param("coverageType", finalCoverageTypeString)
                .param("statuses", finalStatusString)
                .param("clinicIds", finalClinicIdsString)
                .toString();
    }


    @RequestMapping("/private_corporate_patient_listing_paginated")
    public String privateCorporatePatientListingPaginated(Principal principal, HttpServletRequest request) throws Exception {
        ReportDesignOverrider designOverrider = new ReportDesignOverrider(birtReportConfiguration);
        designOverrider.generateReport(request, null, "27_private_corporate_patient_listing_paginated_cmsdua.rptdesign");

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef = new TypeReference() {
        };
        Map<String, String> bodyMap = mapper.readValue(request.getParameter("body"), typeRef);

        String startDate = bodyMap.get("startDate");
        String endDate = bodyMap.get("endDate");
        if (startDate == null || endDate == null) {
            throw new Exception("Invalid Parameter ");
        }

        String finalDoctorIdsString = verifyDoctorRoles(principal, bodyMap, false);

        String statusString = bodyMap.get("statuses");
        String[] status = statusString == null ? new String[0] : statusString.split(",");
        String finalStatusString = formatBirtArray(status);

        String coverageTypeString = bodyMap.get("coverageType");
        String[] coverageType = coverageTypeString == null ? new String[0] : coverageTypeString.split(",");
        String finalCoverageTypeString = formatBirtArray(coverageType);

        String finalClinicIdsString = verifyClinicRoles(principal, bodyMap);

        return new HttpRequestEncoder("redirect:frameset")
                .param("__report", "27_private_corporate_patient_listing_paginated_cmsdua.rptdesign")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("doctorIds", finalDoctorIdsString)
                .param("coverageType", finalCoverageTypeString)
                .param("statuses", finalStatusString)
                .param("clinicIds", finalClinicIdsString)
                .toString();
    }


    @RequestMapping("/report_on_outstanding_bill_invoice")
    public String reportOnOutStandingBillInvoice(Principal principal, HttpServletRequest request) throws Exception {
        ReportDesignOverrider designOverrider = new ReportDesignOverrider(birtReportConfiguration);
        designOverrider.generateReport(request, null, "28_report_on_outstanding_bill_invoice.rptdesign");

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef = new TypeReference() {
        };
        Map<String, String> bodyMap = mapper.readValue(request.getParameter("body"), typeRef);

        String startDate = bodyMap.get("startDate");
        String endDate = bodyMap.get("endDate");
        if (startDate == null || endDate == null) {
            throw new Exception("Invalid Parameter");
        }

        String finalClinicIdsString = verifyClinicRoles(principal, bodyMap);
        String finalDoctorIdsString = verifyDoctorRoles(principal, bodyMap, false);

        return new HttpRequestEncoder("redirect:frameset")
                .param("__report", "28_report_on_outstanding_bill_invoice.rptdesign")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("doctorIds", finalDoctorIdsString)
                .param("clinicIds", finalClinicIdsString)
                .param("coverageTypes", bodyMap.get("coverageTypes") == null ? "" : bodyMap.get("coverageTypes"))
                .param("statuses", bodyMap.get("statuses") == null ? "" : bodyMap.get("statuses"))
                .toString();
    }

    @RequestMapping("/weekly_monthly_revenue_report_by_clinic")
    public String weeklyMonthlyRevenueReportByClinic(Principal principal, HttpServletRequest request) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef = new TypeReference() {
        };
        Map<String, String> bodyMap = mapper.readValue(request.getParameter("body"), typeRef);

        String reportName = "true".equals(bodyMap.get("isMonthly")) ?
                "29_weekly_monthly_revenue_report_by_clinic_month.rptdesign" :
                "29_weekly_monthly_revenue_report_by_clinic_week.rptdesign";

        ReportDesignOverrider designOverrider = new ReportDesignOverrider(birtReportConfiguration);
        designOverrider.generateReport(request, null, reportName);

        String startDate = bodyMap.get("startDate");
        String endDate = bodyMap.get("endDate");
        if (startDate == null || endDate == null) {
            throw new Exception("Invalid Parameter");
        }

        String finalClinicIdsString = verifyClinicRoles(principal, bodyMap);

        return new HttpRequestEncoder("redirect:frameset")
                .param("__report", reportName)
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("clinicIds", finalClinicIdsString)
                .toString();
    }


    @RequestMapping("/ioc_patient")
    public String iocPatient(Principal principal, HttpServletRequest request) throws Exception {
        ReportDesignOverrider designOverrider = new ReportDesignOverrider(birtReportConfiguration);
        designOverrider.generateReport(request, null, "30-ioc_patient.rptdesign");

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef = new TypeReference() {
        };
        Map<String, String> bodyMap = mapper.readValue(request.getParameter("body"), typeRef);

        String startDate = bodyMap.get("startDate");
        String endDate = bodyMap.get("endDate");
        if (startDate == null || endDate == null) {
            throw new Exception("Invalid Parameter");
        }

        String finalClinicIdsString = verifyClinicRoles(principal, bodyMap);

        return new HttpRequestEncoder("redirect:frameset")
                .param("__report", "30-ioc_patient.rptdesign")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("clinicIds", finalClinicIdsString)
                .toString();
    }


    @RequestMapping("/corporate_revenue_summary")
    public String corporateRevenueSummary(HttpServletRequest request) throws Exception {
        ReportDesignOverrider designOverrider = new ReportDesignOverrider(birtReportConfiguration);
        designOverrider.generateReport(request, null, "33_corporate_revenue_summary.rptdesign");

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef = new TypeReference() {
        };
        Map<String, String> bodyMap = mapper.readValue(request.getParameter("body"), typeRef);

        String startDate = bodyMap.get("startDate");
        String endDate = bodyMap.get("endDate");
        if (startDate == null || endDate == null) {
            throw new Exception("Invalid Parameter");
        }
        return new HttpRequestEncoder("redirect:frameset")
                .param("__report", "33_corporate_revenue_summary.rptdesign")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .toString();
    }


    @RequestMapping("/laboratory_service_report")
    public String laboratoryServiceReport(Principal principal, HttpServletRequest request) throws Exception {
        ReportDesignOverrider designOverrider = new ReportDesignOverrider(birtReportConfiguration);
        designOverrider.generateReport(request, null, "34_laboratory_service_report.rptdesign");

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef = new TypeReference() {
        };
        Map<String, String> bodyMap = mapper.readValue(request.getParameter("body"), typeRef);

        String startDate = bodyMap.get("startDate");
        String endDate = bodyMap.get("endDate");
        if (startDate == null || endDate == null) {
            throw new Exception("Invalid Parameter");
        }

        String finalClinicIdsString = verifyClinicRoles(principal, bodyMap);

        return new HttpRequestEncoder("redirect:frameset")
                .param("__report", "34_laboratory_service_report.rptdesign")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("clinicIds", finalClinicIdsString)
                .toString();
    }


    @RequestMapping("/doctor_revenue_report")
    public String doctorRevenueReport(Principal principal, HttpServletRequest request) throws Exception {
        ReportDesignOverrider designOverrider = new ReportDesignOverrider(birtReportConfiguration);
        designOverrider.generateReport(request, null, "40_doctor_revenue_report_cmsdua.rptdesign");

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef = new TypeReference() {
        };
        Map<String, String> bodyMap = mapper.readValue(request.getParameter("body"), typeRef);

        String startDate = bodyMap.get("startDate");
        String endDate = bodyMap.get("endDate");
        if (startDate == null || endDate == null) {
            throw new Exception("Invalid Parameter");
        }

        String finalDoctorIdsString = verifyDoctorRoles(principal, bodyMap, true);

        return new HttpRequestEncoder("redirect:frameset")
                .param("__report", "40_doctor_revenue_report_cmsdua.rptdesign")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("doctorIds", finalDoctorIdsString)
                .toString();
    }

    @RequestMapping("/report_on_treatment")
    public String reportOnTreatment(Principal principal, HttpServletRequest request) throws Exception {
        ReportDesignOverrider designOverrider = new ReportDesignOverrider(birtReportConfiguration);
        designOverrider.generateReport(request, null, "41_report_on_treatment_cmsdua.rptdesign");

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef = new TypeReference() {
        };
        Map<String, String> bodyMap = mapper.readValue(request.getParameter("body"), typeRef);

        String startDate = bodyMap.get("startDate");
        String endDate = bodyMap.get("endDate");
        if (startDate == null || endDate == null) {
            throw new Exception("Invalid Parameter");
        }

        String finalDoctorIdsString = verifyDoctorRoles(principal, bodyMap, false);
        String finalClinicIdsString = verifyClinicRoles(principal, bodyMap);

        return new HttpRequestEncoder("redirect:frameset")
                .param("__report", "41_report_on_treatment_cmsdua.rptdesign")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("doctorIds", finalDoctorIdsString)
                .param("clinicIds", finalClinicIdsString)
                .toString();
    }


    @RequestMapping("/report_on_procedure_category")
    public String reportonProcedureCategory(Principal principal, HttpServletRequest request) throws Exception {
        ReportDesignOverrider designOverrider = new ReportDesignOverrider(birtReportConfiguration);
        designOverrider.generateReport(request, null, "43_report_on_procedure_category.rptdesign");

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef = new TypeReference() {
        };
        Map<String, String> bodyMap = mapper.readValue(request.getParameter("body"), typeRef);

        String startDate = bodyMap.get("startDate");
        String endDate = bodyMap.get("endDate");
        if (startDate == null || endDate == null) {
            throw new Exception("Invalid Parameter");
        }

        String finalDoctorIdsString = verifyDoctorRoles(principal, bodyMap, true);
        String finalClinicIdsString = verifyClinicRoles(principal, bodyMap);

        return new HttpRequestEncoder("redirect:frameset")
                .param("__report", "43_report_on_procedure_category.rptdesign")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("doctorIds", finalDoctorIdsString)
                .param("clinicIds", finalClinicIdsString)
                .toString();
    }

    @RequestMapping("/doctors_patient_seen")
    public String doctorsPatientSeen(Principal principal, HttpServletRequest request) throws Exception {
        ReportDesignOverrider designOverrider = new ReportDesignOverrider(birtReportConfiguration);
        designOverrider.generateReport(request, null, "45_doctors_patient_seen_cmsdua.rptdesign");

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef = new TypeReference() {
        };
        Map<String, String> bodyMap = mapper.readValue(request.getParameter("body"), typeRef);

        String startDate = bodyMap.get("startDate");
        String endDate = bodyMap.get("endDate");
        if (startDate == null || endDate == null) {
            throw new Exception("Invalid Parameter");
        }

        String finalDoctorIdsString = verifyDoctorRoles(principal, bodyMap, true);

        return new HttpRequestEncoder("redirect:frameset")
                .param("__report", "45_doctors_patient_seen_cmsdua.rptdesign")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("doctorIds", finalDoctorIdsString)
                .toString();
    }


    @RequestMapping("/report_on_patient_referral")
    public String reportOnPatientReferral(Principal principal, HttpServletRequest request) throws Exception {


        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef = new TypeReference() {
        };
        Map<String, String> bodyMap = mapper.readValue(request.getParameter("body"), typeRef);

        String reportName = "true".equals(bodyMap.get("showReason")) ?
                "46_report_on_patient_referral_with_reason_cmsdua.rptdesign" :
                "46_report_on_patient_referral_cmsdua.rptdesign";

        ReportDesignOverrider designOverrider = new ReportDesignOverrider(birtReportConfiguration);
        designOverrider.generateReport(request, null, reportName);

        String startDate = bodyMap.get("startDate");
        String endDate = bodyMap.get("endDate");
        if (startDate == null || endDate == null) {
            throw new Exception("Invalid Parameter");
        }

        String finalDoctorIdsString = verifyDoctorRoles(principal, bodyMap, true);

        return new HttpRequestEncoder("redirect:frameset")
                .param("__report", reportName)
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("doctorIds", finalDoctorIdsString)
                .toString();
    }


    @RequestMapping("/report_on_weekly_revenue_summary")
    public String reportOnWeeklyRevenueSummary(Principal principal, HttpServletRequest request) throws Exception {
        ReportDesignOverrider designOverrider = new ReportDesignOverrider(birtReportConfiguration);
        designOverrider.generateReport(request, null, "47_report_on_weekly_revenue_summary_cmsdua.rptdesign");

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef = new TypeReference() {
        };
        Map<String, String> bodyMap = mapper.readValue(request.getParameter("body"), typeRef);

        String startDate = bodyMap.get("startDate");
        String endDate = bodyMap.get("endDate");
        if (startDate == null || endDate == null) {
            throw new Exception("Invalid Parameter");
        }

        String finalDoctorIdsString = verifyDoctorRoles(principal, bodyMap, true);
        String finalClinicIdsString = verifyClinicRoles(principal, bodyMap);
        return new HttpRequestEncoder("redirect:frameset")
                .param("__report", "47_report_on_weekly_revenue_summary_cmsdua.rptdesign")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("clinicIds", finalClinicIdsString)
                .param("doctorIds", finalDoctorIdsString)
                .toString();
    }

    @RequestMapping("/report_on_revenue_summary")
    public String reportOnRevenueSummary(Principal principal, HttpServletRequest request) throws Exception {
        ReportDesignOverrider designOverrider = new ReportDesignOverrider(birtReportConfiguration);
        designOverrider.generateReport(request, null, "48_report_on_revenue_summary.rptdesign");

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef = new TypeReference() {
        };
        Map<String, String> bodyMap = mapper.readValue(request.getParameter("body"), typeRef);

        String startDate = bodyMap.get("startDate");
        String endDate = bodyMap.get("endDate");
        if (startDate == null || endDate == null) {
            throw new Exception("Invalid Parameter");
        }

        String finalDoctorIdsString = verifyDoctorRoles(principal, bodyMap, true);

        return new HttpRequestEncoder("redirect:frameset")
                .param("__report", "48_report_on_revenue_summary.rptdesign")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("doctorIds", finalDoctorIdsString)
                .toString();
    }


    @RequestMapping("/medical_chit")
    public String medicalChit(Principal principal, HttpServletRequest request) throws Exception {
        ReportDesignOverrider designOverrider = new ReportDesignOverrider(birtReportConfiguration);
        designOverrider.generateReport(request, null, "medical_chit.rptdesign");

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef = new TypeReference() {
        };
        Map<String, String> bodyMap = mapper.readValue(request.getParameter("body"), typeRef);

        String startDate = bodyMap.get("startDate");
        String endDate = bodyMap.get("endDate");
        if (startDate == null || endDate == null) {
            throw new Exception("Invalid Parameter");
        }

        String clinicId = bodyMap.get("clinicId");
        logger.info("clinicId: " + clinicId + "|");
        clinicId = clinicId == null || "null".equals(clinicId) ? "" : clinicId;
        String coverageCode = bodyMap.get("coverageCode");
        logger.info("coverageCode: " + coverageCode + "|");
        coverageCode = coverageCode == null || "null".equals(coverageCode) ? "" : coverageCode;
        String patientId = bodyMap.get("patientId");
        logger.info("patientId: " + patientId + "|");
        patientId = patientId == null || "null".equals(patientId) ? "" : patientId;

        String result = new HttpRequestEncoder("redirect:frameset")
                .param("__report", "medical_chit.rptdesign")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("clinicId", clinicId)
                .param("coverageCode", coverageCode)
                .param("patientId", patientId)
                .toString();

        logger.info("RESULT: " + result);
        return result;
    }


    @RequestMapping("/report_on_sms_utilization")
    public String reportOnSmsUtilization(HttpServletRequest request) throws Exception {
        ReportDesignOverrider designOverrider = new ReportDesignOverrider(birtReportConfiguration);
        designOverrider.generateReport(request, null, "49_report_on_sms_utilization.rptdesign");

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef = new TypeReference() {
        };
        Map<String, String> bodyMap = mapper.readValue(request.getParameter("body"), typeRef);

        String startDate = bodyMap.get("startDate");
        String endDate = bodyMap.get("endDate");
        if (startDate == null || endDate == null) {
            throw new Exception("Invalid Parameter");
        }
        return new HttpRequestEncoder("redirect:frameset")
                .param("__report", "49_report_on_sms_utilization.rptdesign")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .toString();
    }


    @RequestMapping(value = "transaction-report-action")
    public String onSubmit(@ModelAttribute("transactionForm") TransactionForm form, BindingResult result,
                           HttpServletRequest request, HttpServletResponse resp) {


        String[] transactionTypes = request.getParameterValues("transactionType");
        String[] selectedColumn = form.getSelectedColumns();
        TransactionQueryBuilder queryBuilder = new TransactionQueryBuilder(birtReportConfiguration);
        String queryString = "";

        List<String> columnList = new ArrayList<>(Arrays.asList(Optional.ofNullable(selectedColumn).orElse(new String[0])));

        DynamicTransactionReportDesign report = new DynamicTransactionReportDesign(columnList.size(), birtReportConfiguration);
        HashMap<String, String> params = new HashMap<>();
        params.put("msisdn", form.getMsisdn());
        params.put("startDate", form.getStartDate());
        params.put("endDate", form.getEndDate());
        params.put("sessionId", form.getSessionId());
        params.put("transactionId", form.getTransactionId());
        params.put("transactionType", Arrays.asList(Optional.ofNullable(transactionTypes).orElse(new String[0])).toString());

        try {
            report.generateReport(columnList, queryString, params);
            logger.info("Transaction report has been generated");
            return new HttpRequestEncoder("redirect:frameset").param("__report", BartTemplateNameUtil.TRANSACTION_REPORT).toString();
        } catch (Exception e) {
            logger.error("Error occurred while generating transaction report {} ", e);
            return "transaction";
        }

    }


    @RequestMapping("/10_patient_consultation_history_test")
    public String patientConsultationHistoryTest(HttpServletRequest request, HttpServletResponse resp) throws Exception {
        ReportDesignOverrider designOverrider = new ReportDesignOverrider(birtReportConfiguration);
        designOverrider.generateReport(request, resp, "10_patient_consultation_history_enquiry.rptdesign");
        return new HttpRequestEncoder("redirect:frameset")
                .param("__report", "10_patient_consultation_history_enquiry.rptdesign")
                .param("patientId", "0000001").toString();
    }


    private String verifyClinicRoles(Principal principal, Map<String, String> bodyMap) {

        String clinicIdsString = bodyMap.get("clinicIds");
        String[] clinicIds = (clinicIdsString == null || clinicIdsString.length() == 0 || "null".equals(clinicIdsString)) ? new String[0] : clinicIdsString.split(",");
        String finalClinicIdsString = formatBirtArray(clinicIds);

//        TODO malith : commented out role verifications to test this in localy.
/*        List<String> roles = CommonWebUtil.userRolesAsString(principal);

        if (clinicIds.length == 0 || clinicIds.length == 1 && "".equals(clinicIds[0])) {

            if (roles.stream().noneMatch(CLINIC_BYPASS_ROLES::contains)) {
                throw new IllegalArgumentException("Not allowed to view all clinic report");
            }
        } else {
            List<Clinic> clinics = clinicRepository.findByIdIn(Arrays.asList(clinicIds));
            for (Clinic clinic : clinics) {
                CmsServiceResponse cmsServiceResponse = validateClinicAccess(principal.getName(), roles, clinic);
                if (cmsServiceResponse != null) {
                    logger.error("User [" + principal.getName() + "] has no access to clinic for clinic revenue report [" + clinic.getClinicCode()
                            + "] id [" + clinic.getId() + "]");
                    throw new IllegalArgumentException("Not allowed to view clinic report");
                }
            }
        }*/
        return finalClinicIdsString;
    }

    private String verifyDoctorRoles(Principal principal, Map<String, String> bodyMap, boolean strictMode) {
        String doctorIdsString = bodyMap.getOrDefault("doctorIds", "");
        String[] doctorIds = (doctorIdsString == null || doctorIdsString.length() == 0 || "null".equals(doctorIdsString)) ? new String[0] : doctorIdsString.split(",");

        String clinicIdsString = bodyMap.get("clinicIds");
        String[] clinicIds = (clinicIdsString == null || clinicIdsString.length() == 0 || "null".equals(clinicIdsString)) ? new String[0] : clinicIdsString.split(",");
        Optional<Clinic> clinic = null;
        if(clinicIds.length == 1 && !"".equals(clinicIds[0])) {
            clinic = clinicDatabaseService.findOne(clinicIds[0]);
        }


        //        TODO malith : commented out role verifications to test this in localy.
/*        List<String> roles = CommonWebUtil.userRolesAsString(principal);
        ArrayList<Doctor> doctors = new ArrayList<>();
        if (doctorIds.length > 0) {
            for(String doctorId:doctorIds){
                if(!TextUtils.isEmpty(doctorId)) {
                    doctors.add(doctorRepository.findOne(doctorId));
                }
            }
        }


        if (roles.stream().noneMatch(DOCTOR_BYPASS_ROLES::contains)) {
            //Not bypass role
            if(doctors.size() == 0){
                //no doctor selected
                throw new IllegalArgumentException("Not allowed to view doctor report");
            }

            if(doctors.size() != 1 || !doctors.get(0).getUsername().equals(principal.getName()) ){
                //More than 1 doctors selected or doctor selected is not the user
                if(strictMode || clinic == null){
                    throw new IllegalArgumentException("Not allowed to view doctor report");
                }

                for (Doctor doctor: doctors){
                    if(clinic.getAttendingDoctorId() == null || !clinic.getAttendingDoctorId().contains(doctor.getId())) {
                        throw new IllegalArgumentException("Not allowed to view doctor report");
                    }
                }


            }

        }*/
        return formatBirtArray(doctorIds);
    }

    private String formatBirtArray(String arrayToEscape[]) {

        String[] resultArray = new String[arrayToEscape.length];

        if (arrayToEscape.length > 0 && !"".equals(arrayToEscape[0])) {
            for (int i = 0; i < arrayToEscape.length; i++) {
                resultArray[i] = '"' + arrayToEscape[i] + '"';
            }
        }
        String result = Arrays.toString(resultArray);
        result = result.substring(1, result.length() - 1);
        return result;
    }

    private static CmsServiceResponse validateClinicAccess(String username, List<String> userRoles, Clinic clinic) {
        if (clinic == null) {
            logger.error("Clinic cannot be null");
            return new CmsServiceResponse<>(StatusCode.E2000, "Clinic not available");
        } else {
            logger.debug("Checking for user [{}] byPassRoles[{}]", username, TransactionController.CLINIC_BYPASS_ROLES);
            boolean userAllowed = userRoles
                    .stream()
                    .anyMatch(TransactionController.CLINIC_BYPASS_ROLES::contains) ||
                    clinic.getClinicStaffUsernames().stream().anyMatch(s -> s.equals(username));
            if (!userAllowed) {
                String message = "User [" + username + "] not allowed to use this clinic ["+ clinic.getId() + "] code[" + clinic.getClinicCode() + "]";
                logger.error(message);
                return new CmsServiceResponse<>(StatusCode.E2000, message);
            }
        }
        return null;
    }
}
