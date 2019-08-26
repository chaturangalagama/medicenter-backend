package com.ilt.cms.report.ui.models;

import com.ilt.cms.report.ui.models.dataset.CommonDataSet;
import com.ilt.cms.report.ui.service.WeeklyRevenueSummaryReportService;
import com.ilt.cms.report.ui.util.Formatter;
import org.eclipse.birt.data.oda.pojo.api.IPojoDataSet;
import org.eclipse.datatools.connectivity.oda.OdaException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WeeklyRevenueSummaryReport extends CommonDataSet implements IPojoDataSet {

    private Float visits;

    private Iterator<WeeklyRevenueSummaryReport> iterator;

    public WeeklyRevenueSummaryReport() {
    }

    public WeeklyRevenueSummaryReport(String clinicCode, Date startTime, Float visits, Float patientPayable, Float corporatePayable,
                                      Float insurancePayable, Float chasPayable, Float mediSavePayable) {
        setClinicCode(clinicCode);
        setStartTime(startTime);
        this.visits = visits;
        setPatientPayable(patientPayable);
        setCorporatePayable(corporatePayable);
        setInsurancePayable(insurancePayable);
        setChasPayable(chasPayable);
        setMediSavePayable(mediSavePayable);
    }

    @Override
    public void open(Object o, Map<String, Object> map) throws OdaException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(String.valueOf(map.get("startDate")), formatter);
        LocalDate endDate = LocalDate.parse(String.valueOf(map.get("endDate")), formatter);
        List<String> clinicIds = Formatter.asList(String.valueOf(map.get("clinicIds")));

        iterator = new WeeklyRevenueSummaryReportService()
                .getPatientTreatmentRecord(clinicIds, LocalDateTime.of(startDate, LocalTime.MIN), LocalDateTime.of(endDate, LocalTime.MAX))
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

    public Float getVisits() {
        return visits;
    }

    public void setVisits(Float visits) {
        this.visits = visits;
    }
}
