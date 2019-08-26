package com.ilt.cms.report.ui.models;

import com.ilt.cms.report.ui.models.dataset.CommonDataSet;
import com.ilt.cms.report.ui.models.dataset.DailyQueueListItem;
import com.ilt.cms.report.ui.service.DailyQueueListReportService;
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

public class DailyQueueListReport extends CommonDataSet implements IPojoDataSet {

    private List<DailyQueueListItem> itemList;

    private Iterator<DailyQueueListReport> iterator;

    public DailyQueueListReport() {
    }

    public DailyQueueListReport(Date startTime, Date endTime,
                                String clincCode, String clinicName, String patientNumber,
                                String patientIdentity, String patientName, String patientGender,
                                String doctorName, Float totalBillAmount,List<DailyQueueListItem> list
    ) {
        setStartTime(startTime);
        setEndTime(endTime);
        setClinicCode(clincCode);
        setClinicName(clinicName);
        feedPatientData(patientName, patientNumber, patientIdentity);
        setPatientGender(patientGender);
        setDoctorName(doctorName);
        setTotalBillAmount(totalBillAmount);
        itemList = list;
    }

    @Override
    public void open(Object o, Map<String, Object> map) throws OdaException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(String.valueOf(map.get("startDate")), formatter);
        LocalDate endDate = LocalDate.parse(String.valueOf(map.get("endDate")), formatter);
        List<String> clinicIds = Formatter.asList(String.valueOf(map.get("clinicIds")));
        List<String> doctorIds = Formatter.asList(String.valueOf(map.get("doctorIds")));
        Boolean isDetail = Boolean.parseBoolean(String.valueOf(map.get("isDetail")));

        iterator = new DailyQueueListReportService()
                .getDailyQueueListReport(doctorIds, clinicIds, LocalDateTime.of(startDate, LocalTime.MIN), LocalDateTime.of(endDate, LocalTime.MAX))
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

    public List<DailyQueueListItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<DailyQueueListItem> itemList) {
        this.itemList = itemList;
    }
}
