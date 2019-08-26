
package com.ilt.cms.report.ui.builder;

import com.ilt.cms.report.ui.report.BirtReportConfiguration;

import java.util.List;

public class TransactionQueryBuilder {
    private static StringBuilder queryBuilder;
    private BirtReportConfiguration birtReportConfiguration;

    public TransactionQueryBuilder(BirtReportConfiguration birtReportConfiguration) {
        this.birtReportConfiguration = birtReportConfiguration;
    }

    public TransactionQueryBuilder init(String msisdn, String startDate, String endDate) {
        queryBuilder = new StringBuilder();

        //Used all the columns in the table instead of '*' to Set Aliases for Service Code, status Codes from the
        //SQL level it self rather than Birt level since if Alias is set from Birt Level
        //then the downloaded file (CSV, XLS) from Birt will not have the aliases.
        queryBuilder.append("SELECT date_time, transaction_id, session_id, system_id, app_id, msisdn, menu_classification, " +
                "menu_description");


        queryBuilder.append(" service_code, complete_path, charged_amount, location, system_status_code, system_status_description, " +
                "ussd_status_code, ussd_status_description, sms_status_code, sms_status_description FROM transaction ");

        if (msisdn.isEmpty()) {
            queryBuilder.append(" WHERE ");
        } else {
            queryBuilder.append(" WHERE msisdn='").append(msisdn).append("' AND ");
        }
        queryBuilder.append(" (date_time BETWEEN '").append(startDate);
        queryBuilder.append("' AND '").append(endDate).append("') ");
        return this;
    }


    public TransactionQueryBuilder setSessionId(String sessionId) {
        if (!isEmpty(sessionId)) {
            queryBuilder.append(" AND session_id like ").append("'%").append(sessionId).append("%' ");
        }
        return this;
    }

    public TransactionQueryBuilder setTransactionId(String transactionId) {
        if (!isEmpty(transactionId)) {
            queryBuilder.append(" AND transaction_id like ").append("'%").append(transactionId).append("%'");
        }
        return this;
    }

    public TransactionQueryBuilder setTransactionTypes(List<String> transactionTypes) {
        if (transactionTypes.size() > 0) {
            queryBuilder.append(" AND ussd_transaction_type IN (");
            for (String str : transactionTypes) {
                queryBuilder.append("'").append(str).append("',");
            }
            queryBuilder.deleteCharAt(queryBuilder.length() - 1);
            queryBuilder.append(")");
        }
        return this;
    }


    public String build() {
        return queryBuilder.toString();
    }

    private boolean isEmpty(String value) {
        return value == null || value.equals("");
    }

}
