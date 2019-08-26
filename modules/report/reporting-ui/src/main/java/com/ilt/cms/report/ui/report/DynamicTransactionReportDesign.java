package com.ilt.cms.report.ui.report;

import com.ilt.cms.report.ui.util.BartTemplateNameUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.birt.report.model.api.*;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.elements.structures.MapRule;
import org.eclipse.birt.report.model.elements.ReportItem;

import java.util.*;

public class DynamicTransactionReportDesign {

    public static final Logger LOGGER = LogManager.getLogger(DynamicTransactionReportDesign.class);
    private final BirtReportConfiguration birtReportConfiguration;
    private ReportDesignHandle reportDesign = null;
    private ElementFactory elementFactory = null;
    private Map<String, BirtReportRowMapper> columnBindingMap = new LinkedHashMap<>();
    private SessionHandle session;
    private int noOfColumns;

    public DynamicTransactionReportDesign(int noOfColumns, BirtReportConfiguration birtReportConfiguration) {
        this.noOfColumns = noOfColumns;
        this.birtReportConfiguration = birtReportConfiguration;
    }

    public void generateReport(List<String> columnList, String query, Map<String, String> params) throws Exception {
        if (session == null) {
            session = birtReportConfiguration.createReportSession();
        }
        // Create a new report design.
        reportDesign = session.createDesign();
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
        buildDataSet(query);
        createPageTitle();
        createGridView(params);
        createDataTable(columnList);
    }

    private void createPageTitle() throws SemanticException {
        LabelHandle pageHeader = elementFactory.newLabel("page-header");
        pageHeader.setStyleName("page-header");
        pageHeader.setTextKey("detailed.transaction.report.header");
        reportDesign.getBody().add(pageHeader);
    }

    private class SearchParameter {

        private int rowNumber;
        private String labelName;
        private String valueText;

        private SearchParameter(int rowNumber, String labelName, String valueText) {
            this.rowNumber = rowNumber;
            this.labelName = labelName;
            this.valueText = valueText;
        }
    }

    private void createGridView(Map<String, String> params) throws SemanticException {
        GridHandle grid = elementFactory.newGridItem("Grid", 2, 6);
        grid.setWidth("100%");
        grid.setStyleName("grid");

        List<SearchParameter> searchParameterList = new ArrayList<>();
        searchParameterList.add(new SearchParameter(0, "Msisdn :", params.get("msisdn")));
        searchParameterList.add(new SearchParameter(1, "Start Date & Time :", params.get("startDate")));
        searchParameterList.add(new SearchParameter(2, "End Date & Time :", params.get("endDate")));
        searchParameterList.add(new SearchParameter(3, "Session Id :", params.get("sessionId")));
        searchParameterList.add(new SearchParameter(4, "Transaction Id :", params.get("transactionId")));
        searchParameterList.add(new SearchParameter(5, "Transaction Types :", params.get("transactionType")));

        ColumnHandle firstColumn = (ColumnHandle) grid.getColumns().get(0);
        firstColumn.setProperty("width", 2);

        for (SearchParameter parameter : searchParameterList) {
            RowHandle row = (RowHandle) grid.getRows().get(parameter.rowNumber);
            row.setStyleName("grid-row");
            LabelHandle label = elementFactory.newLabel(null);
            CellHandle cell = (CellHandle) row.getCells().get(0);
            cell.setStyleName("grid-cell");
            cell.getContent().add(label);
            label.setText(parameter.labelName);

            label = elementFactory.newLabel(null);
            cell = (CellHandle) row.getCells().get(1);
            cell.setStyleName("grid-cell");
            cell.getContent().add(label);
            label.setText(parameter.valueText);
        }
        reportDesign.getBody().add(grid);
    }

    private void createDataTable(List<String> columnsList) throws Exception {
        TableHandle table = elementFactory.newTableItem("table", noOfColumns, 1, 1, 1);
        table.setPageBreakInterval(birtReportConfiguration.getNoOfRowsPerPage());
        table.setWidth("100%");
        table.setProperty("whiteSpace", "nowrap");
        table.setStyleName("table-style");
        table.setDataSet(reportDesign.findDataSet("ds"));
        bindColumns(table, columnsList);

        buildTableHeader(table, columnsList);

        //Details row...
        RowHandle detailsRow = (RowHandle) table.getDetail().get(0);
        detailsRow.setOnCreate("countOfRows++;");
        int i = 0;
        for (Map.Entry<String, BirtReportRowMapper> entry : columnBindingMap.entrySet()) {
            if (columnsList.contains(entry.getKey())) {
                CellHandle cell = (CellHandle) detailsRow.getCells().get(i++);
                DataItemHandle data = elementFactory.newDataItem(null);
                data.setResultSetColumn(entry.getValue().getComputedColumn().getName());
                cell.getContent().add(data);
                if ("service_code".equals(entry.getKey())) {
                    addMapRule(cell, "row[\"service_code\"]", Collections.emptyMap());
                }
                if("category".equals(entry.getKey())){
                    addMapRule(cell, "row[\"category\"]", Collections.emptyMap());
                }
            }
        }

        RowHandle footerRow = (RowHandle) table.getFooter().get(0);

        for (int j = 0; j < noOfColumns - 1; j++) {
            footerRow.getCells().get(0).drop();
        }

//        CellHandle cell = (CellHandle) footerRow.getCells().get(0);
//        cell.setColumnSpan(noOfColumns);
//        LabelHandle label = elementFactory.newLabel(null);
//        label.setText("No Data Available");
//        cell.setStyleName("table-footer-row");
//        cell.setColumnSpan(noOfColumns);
//        cell.setProperty(StyleHandle.TEXT_ALIGN_PROP, "center");
//        cell.getContent().add(label);

        HideRule hr = StructureFactory.createHideRule();
        hr.setFormat("all");
        hr.setExpression("countOfRows!=0");

        PropertyHandle ph = footerRow.getPropertyHandle(ReportItem.VISIBILITY_PROP);
        ph.addItem(hr);


        //Add table to the report's body
        reportDesign.getBody().add(table);

        // Save the design and close it.
        reportDesign.saveAs(birtReportConfiguration.getReportLocation() + "/" + BartTemplateNameUtil.TRANSACTION_REPORT);
        reportDesign.close();
        LOGGER.info("Report generation done!");
    }

    /**
     * Set a alias to a given value
     * @param cellHandle
     * @param column
     * @param map
     * @throws Exception
     */
    private void addMapRule(CellHandle cellHandle, String column, Map<String, String> map) throws Exception {
        PropertyHandle ph = cellHandle.getPropertyHandle(StyleHandle.MAP_RULES_PROP);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            MapRule rule = StructureFactory.createMapRule();
            rule.setTestExpression(column);
            rule.setOperator(DesignChoiceConstants.MAP_OPERATOR_EQ);
            rule.setValue1("\"" + entry.getKey() + "\"");
            rule.setDisplay(entry.getValue());
            ph.addItem(rule);
        }
    }

    private void bindColumns(TableHandle table, List<String> columnsList) throws SemanticException {
        PropertyHandle computedSet = table.getColumnBindings();
        columnBindingMap.put("transaction_id", new BirtReportRowMapper(null, "Transaction Id", "dataSetRow[\"transaction_id\"]", "decimal"));
        columnBindingMap.put("session_id", new BirtReportRowMapper(null, "Session Id", "dataSetRow[\"session_id\"]", "decimal"));
        columnBindingMap.put("msisdn", new BirtReportRowMapper(null, "Msisdn", "dataSetRow[\"msisdn\"]", "decimal"));
        columnBindingMap.put("system_id", new BirtReportRowMapper(null, "System Id", "dataSetRow[\"system_id\"]", "String"));
        columnBindingMap.put("app_id", new BirtReportRowMapper(null, "App Id", "dataSetRow[\"app_id\"]", "String"));
        columnBindingMap.put("menu_description", new BirtReportRowMapper(null, "Menu Description", "dataSetRow[\"menu_description\"]", "String"));
        columnBindingMap.put("menu_classification", new BirtReportRowMapper(null, "Menu Classification", "dataSetRow[\"menu_classification\"]", "String"));
        columnBindingMap.put("category", new BirtReportRowMapper(null, "Category", "dataSetRow[\"category\"]", "String"));
        columnBindingMap.put("user_language", new BirtReportRowMapper(null, "User Language", "dataSetRow[\"user_language\"]", "String"));
        columnBindingMap.put("ussd_transaction_type", new BirtReportRowMapper(null, "Ussd Transaction Type", "dataSetRow[\"ussd_transaction_type\"]", "String"));
        columnBindingMap.put("user_message_sent_time", new BirtReportRowMapper(null, "User Message Sent Time", "dataSetRow[\"user_message_sent_time\"]", "String"));
        columnBindingMap.put("user_response_received_time", new BirtReportRowMapper(null, "User Response Received Time", "dataSetRow[\"user_response_received_time\"]", "String"));
        columnBindingMap.put("user_response_duration", new BirtReportRowMapper(null, "User Response Duration", "dataSetRow[\"user_response_duration\"]", "String"));
        columnBindingMap.put("session_start_date_time", new BirtReportRowMapper(null, "Session Start Date Time", "dataSetRow[\"session_start_date_time\"]", "String"));
        columnBindingMap.put("session_end_date_time", new BirtReportRowMapper(null, "Session End Date Time", "dataSetRow[\"session_end_date_time\"]", "String"));
        columnBindingMap.put("session_duration", new BirtReportRowMapper(null, "Session Duration", "dataSetRow[\"session_duration\"]", "String"));
        columnBindingMap.put("service_code", new BirtReportRowMapper(null, "Service Code", "dataSetRow[\"service_code\"]", "String"));
        columnBindingMap.put("complete_path", new BirtReportRowMapper(null, "Complete Path", "dataSetRow[\"complete_path\"]", "String"));
        columnBindingMap.put("charged_amount", new BirtReportRowMapper(null, "Charged Amount", "dataSetRow[\"charged_amount\"]", "String"));
        columnBindingMap.put("location", new BirtReportRowMapper(null, "Location", "dataSetRow[\"location\"]", "String"));
        columnBindingMap.put("system_status_code", new BirtReportRowMapper(null, "System Status Code", "dataSetRow[\"system_status_code\"]", "String"));
        columnBindingMap.put("system_status_description", new BirtReportRowMapper(null, "System Status Description", "dataSetRow[\"system_status_description\"]", "String"));
        columnBindingMap.put("ussd_status_code", new BirtReportRowMapper(null, "Ussd Status Code", "dataSetRow[\"ussd_status_code\"]", "String"));
        columnBindingMap.put("ussd_status_description", new BirtReportRowMapper(null, "Ussd Status Description", "dataSetRow[\"ussd_status_description\"]", "String"));
        columnBindingMap.put("sms_status_code", new BirtReportRowMapper(null, "Sms Status Code", "dataSetRow[\"sms_status_code\"]", "String"));
        columnBindingMap.put("sms_status_description", new BirtReportRowMapper(null, "Sms Status Description", "dataSetRow[\"sms_status_description\"]", "String"));

        for (Map.Entry<String, BirtReportRowMapper> entry : columnBindingMap.entrySet()) {
            if (columnsList.contains(entry.getKey())) {
                BirtReportRowMapper model = entry.getValue();
                model.setComputedColumn(StructureFactory.createComputedColumn());
                ComputedColumn computedColumn = model.getComputedColumn();
                computedColumn.setName(entry.getKey());
                computedColumn.setDisplayName(model.getColumnName());
                computedColumn.setExpression(model.getExpression());
                computedColumn.setDataType(model.getDataType());
                computedSet.addItem(computedColumn);
                model.setComputedColumn(model.getComputedColumn());
            }
        }
    }


    private void buildTableHeader(TableHandle table, List<String> columnsList) throws SemanticException {
        RowHandle header = (RowHandle) table.getHeader().get(0);
        int columnIndex = 0;
        for (Map.Entry<String, BirtReportRowMapper> entry : columnBindingMap.entrySet()) {
            if (columnsList.contains(entry.getKey())) {

                int posn = columnIndex++;
                ColumnHandle ch = (ColumnHandle) table.getColumns().get(posn);
                ch.getWidth().setStringValue("2in");

                CellHandle cell = (CellHandle) header.getCells().get(posn);
                LabelHandle label = elementFactory.newLabel(null);
                label.setText(entry.getValue().getColumnName());
                cell.setStyleName("table-header-cell");
                cell.getContent().add(label);
            }
        }
    }

    public void buildDataSource() throws SemanticException {
        OdaDataSourceHandle dataSource = elementFactory.newOdaDataSource("Data Source", "org.eclipse.birt.report.data.oda.jdbc");
        dataSource.setProperty("odaDriverClass", birtReportConfiguration.getMySqlDriver());
        dataSource.setProperty("odaURL", birtReportConfiguration.getMySqlUrl());
        dataSource.setProperty("odaUser", birtReportConfiguration.getMySqlUsername());
        dataSource.setProperty("odaPassword", birtReportConfiguration.getMySqlPassword());
        reportDesign.getDataSources().add(dataSource);
    }

    void buildDataSet(String qry) throws SemanticException {
        OdaDataSetHandle dsHandle = elementFactory.newOdaDataSet("ds", "org.eclipse.birt.report.data.oda.jdbc.JdbcSelectDataSet");
        dsHandle.setDataSource("Data Source");
        dsHandle.setQueryText(qry);
        reportDesign.getDataSets().add(dsHandle);
    }
}