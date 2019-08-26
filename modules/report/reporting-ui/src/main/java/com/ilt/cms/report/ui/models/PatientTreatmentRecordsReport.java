package com.ilt.cms.report.ui.models;

import com.ilt.cms.report.ui.models.dataset.CommonDataSet;
import com.ilt.cms.report.ui.models.dataset.PatientTreatmentItem;
import com.ilt.cms.report.ui.service.PatientTreatmentRecordsReportService;
import com.ilt.cms.report.ui.util.Formatter;
import org.eclipse.birt.data.oda.pojo.api.IPojoDataSet;
import org.eclipse.datatools.connectivity.oda.OdaException;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PatientTreatmentRecordsReport extends CommonDataSet implements IPojoDataSet, Serializable {

    private List<PatientTreatmentItem> items;

    private Iterator<PatientTreatmentRecordsReport> iterator;

    @Override
    public void open(Object o, Map<String, Object> map) throws OdaException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate startDate = LocalDate.parse(map.get("startDate").toString(), formatter);
        LocalDate endDate = LocalDate.parse(map.get("endDate").toString(), formatter);

        List<String> clinicIds = Formatter.asList(String.valueOf(map.get("clinicIds")));
        List<String> doctorIds = Formatter.asList(String.valueOf(map.get("doctorIds")));
        List<String> itemCodes = Formatter.asList(String.valueOf(map.get("itemCodes")));
        List<String> itemCategoryCodes = Formatter.asList(String.valueOf(map.get("itemCategoryCodes")));
        List<String> financialPlanIds = Formatter.asList(String.valueOf(map.get("financialPlanId")));

        iterator = new PatientTreatmentRecordsReportService().getPatientTreatmentRecord(
                clinicIds, doctorIds, financialPlanIds, itemCategoryCodes, itemCodes,
                LocalDateTime.of(startDate, LocalTime.MIN), LocalDateTime.of(endDate, LocalTime.MAX))
                .iterator();
    }

    @Override
    public Object next() throws OdaException {
        if (iterator.hasNext())
            return iterator.next();
        return null;
    }

    @Override
    public void close() throws OdaException {
        iterator = null;
    }

    public List<PatientTreatmentItem> getItems() {
        return items;
    }

    public void setItems(List<PatientTreatmentItem> items) {
        this.items = items;
    }
}
