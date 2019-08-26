package com.ilt.cms.report.ui.report;

import org.eclipse.birt.report.model.api.*;
import org.eclipse.birt.report.model.api.activity.SemanticException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ReportDesignOverrider {

    private final BirtReportConfiguration birtReportConfiguration;
    private ReportDesignHandle reportDesign = null;
    private ElementFactory elementFactory = null;
    private SessionHandle session;

    public ReportDesignOverrider(BirtReportConfiguration birtReportConfiguration) {
        this.birtReportConfiguration = birtReportConfiguration;
    }

    public void generateReport(HttpServletRequest request, HttpServletResponse resp, String report) throws Exception {
        if (session == null) {
            session = birtReportConfiguration.createReportSession();
        }
//        InputStream reportAsStream = ReportDesignOverrider.class.getResourceAsStream("/report/10_patient_consultation_history_enquiry.rptdesign");
        // Create a new report design.
        reportDesign = session.createDesign();
//        reportDesign.
        reportDesign.setInitialize("countOfRows=0;");
        reportDesign.setLayoutPreference("auto layout");
        elementFactory = reportDesign.getElementFactory();
        reportDesign.addCss(birtReportConfiguration.getReportCssLocation());
        reportDesign.setIncludeResource(birtReportConfiguration.getReportResourceLocation());
        SimpleMasterPageHandle masterPage = elementFactory.newSimpleMasterPage("Page Master");

        DesignElementHandle footerText = elementFactory.newTextItem("footer");
        footerText.setStyleName("copyright-footer");
        footerText.setProperty("contentType", "html");
        footerText.setStringProperty("content", "Copyright &copy 2018 <a href=\"http://www.ouelh.com\" " +
                "target=\"_blank\" style=\"color:#000000\">OUE LH</a> All Rights Reserved.");

        masterPage.getPageFooter().add(footerText);

        masterPage.setShowFooterOnLast(true);
        reportDesign.getMasterPages().add(masterPage);

        buildDataSource();
    }


    public void buildDataSource() throws SemanticException {
        OdaDataSourceHandle dataSource = elementFactory.newOdaDataSource("Data Source", "org.eclipse.birt.report.data.oda.jdbc");
//        DataSourceHandle dataSource = reportDesign.findDataSource("Data Source");
        dataSource.setProperty("odaDriverClass", birtReportConfiguration.getMySqlDriver());
        dataSource.setProperty("odaURL", birtReportConfiguration.getMySqlUrl());
        dataSource.setProperty("odaUser", birtReportConfiguration.getMySqlUsername());
        dataSource.setProperty("odaPassword", birtReportConfiguration.getMySqlPassword());
        reportDesign.getDataSources().add(dataSource);
    }
}