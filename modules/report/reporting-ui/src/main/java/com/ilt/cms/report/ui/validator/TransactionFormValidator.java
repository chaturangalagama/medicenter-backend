package com.ilt.cms.report.ui.validator;


import com.ilt.cms.report.ui.domain.TransactionForm;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class TransactionFormValidator extends CommonValidator implements Validator {

    private static final String DATE_TIME_PATTERN = "uuuu-MM-dd HH:mm";
    private String msisdnRegex;

    private int maxDurationInMinutes = 10;

    private int maxPastRecordsInMonths = 2;

    private int maxDurationInDays = 30;

    @Override
    public boolean supports(Class<?> aClass) {
        return TransactionForm.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        TransactionForm form = (TransactionForm) object;
        String msisdn = form.getMsisdn();
        String startDateTime = form.getStartDate();
        String endDateTime = form.getEndDate();
        String transactionId = form.getTransactionId();
        String sessionId = form.getSessionId();

        if (!isEmpty(msisdn) && !msisdn.matches(msisdnRegex)) {
            errors.rejectValue("msisdn", "transaction.report.msisdn.invalid.error");
        }

        LocalDateTime now = LocalDateTime.now();
        if (isEmpty(startDateTime)) {
            errors.rejectValue("startDate", "transaction.report.start.date.time.empty.error");
        } else {
            LocalDateTime start = LocalDateTime.parse(startDateTime, DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
            if (start.isBefore(now.minusMonths(maxPastRecordsInMonths))) {
                errors.rejectValue("startDate", "transaction.report.start.date.time.invalid.error", new String[]{String.valueOf(maxPastRecordsInMonths)}, null);
            }
            if (start.isAfter(now.minusMinutes(now.getMinute()))) {
                errors.rejectValue("startDate", "transaction.report.start.date.time.invalid.error2");
            }
        }
        if (isEmpty(endDateTime)) {
            errors.rejectValue("endDate", "transaction.report.end.date.time.empty.error");
        } else {
            LocalDateTime end = LocalDateTime.parse(endDateTime, DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
            if (end.isBefore(now.minusMonths(maxPastRecordsInMonths))) {
                errors.rejectValue("endDate", "transaction.report.end.date.time.invalid.error", new String[]{String.valueOf(maxPastRecordsInMonths)}, null);
            }
            if (end.isAfter(now.minusMinutes(now.getMinute()))) {
                errors.rejectValue("endDate", "transaction.report.end.date.time.invalid.error2");
            }
            if (!isEmpty(startDateTime)) {
                LocalDateTime start = LocalDateTime.parse(startDateTime, DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
                if (end.isBefore(start)) {
                    errors.rejectValue("endDate", "transaction.report.start.and.end.date.time.invalid.error");
                } else if (isEmpty(msisdn)) {
                    if (end.minusMinutes(maxDurationInMinutes).isAfter(start)) {
                        errors.rejectValue("endDate", "report.start.end.date.time.range.no.msisdn.error", new String[]{String.valueOf(maxDurationInMinutes)}, null);
                    }
                } else {
                    if (end.minusDays(maxDurationInDays).isAfter(start)) {
                        errors.rejectValue("endDate", "report.start.end.date.range", new String[]{maxDurationInDays + " days"}, null);
                    }
                }
            }
        }

        //sessionId and transactionId validation
        if (!isEmpty(transactionId)) {
            if (!isInteger(transactionId)) {
                errors.rejectValue("transactionId", "transaction.report.transaction.id.not.integer.error");
            }
        }
        if (!isEmpty(sessionId)) {
            if (!isInteger(sessionId)) {
                errors.rejectValue("sessionId", "transaction.report.session.id.not.integer.error");
            }
        }

        if(form.getSelectedColumns().length == 0){
            errors.rejectValue("selectedColumns", "transaction.report.selected.columns.empty.error");
        }
    }

    public void setMsisdnRegex(String msisdnRegex) {
        this.msisdnRegex = msisdnRegex;
    }

    public void setMaxDurationInMinutes(int maxDurationInMinutes) {
        this.maxDurationInMinutes = maxDurationInMinutes;
    }

    public void setMaxPastRecordsInMonths(int maxPastRecordsInMonths) {
        this.maxPastRecordsInMonths = maxPastRecordsInMonths;
    }

    public void setMaxDurationInDays(int maxDurationInDays) {
        this.maxDurationInDays = maxDurationInDays;
    }
}
