package com.ilt.cms.report.ui.models;

import com.ilt.cms.report.ui.models.dataset.CommonDataSet;
import com.ilt.cms.report.ui.service.DoctorRevenueReportService;
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

public class DoctorRevenueReport extends CommonDataSet implements IPojoDataSet, Serializable {

    private Long quantity;
    private Float totalAmount;
    private Long totalVisits;

    private Iterator<DoctorRevenueReport> iterator;

    @Override
    public void open(Object o, Map<String, Object> map) throws OdaException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(String.valueOf(map.get("startDate")), formatter);
        LocalDate endDate = LocalDate.parse(String.valueOf(map.get("endDate")), formatter);
        List<String> doctorIds = Formatter.asList(String.valueOf(map.get("doctorIds")));

        iterator = new DoctorRevenueReportService()
                .getDoctorRevenueReportData(doctorIds, LocalDateTime.of(startDate, LocalTime.MIN), LocalDateTime.of(endDate, LocalTime.MAX))
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


    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getTotalVisits() {
        return totalVisits;
    }

    public void setTotalVisits(Long totalVisits) {
        this.totalVisits = totalVisits;
    }
}
