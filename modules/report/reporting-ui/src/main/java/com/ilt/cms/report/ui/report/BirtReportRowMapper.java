package com.ilt.cms.report.ui.report;


import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;

public class BirtReportRowMapper {

    private ComputedColumn computedColumn;
    private String columnName;
    private String expression;
    private String dataType;


    public BirtReportRowMapper(ComputedColumn computedColumn, String columnName, String expression, String dataType) {
        this.computedColumn = computedColumn;
        this.columnName = columnName;
        this.expression = expression;
        this.dataType = dataType;
    }

    public ComputedColumn getComputedColumn() {
        return computedColumn;
    }

    public void setComputedColumn(ComputedColumn computedColumn) {
        this.computedColumn = computedColumn;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }


    @Override
    public String toString() {
        return "ReportModel{" +
                "computedColumn=" + computedColumn +
                ", columnName='" + columnName + '\'' +
                ", expression='" + expression + '\'' +
                ", dataType='" + dataType + '\'' +
                '}';
    }
}
