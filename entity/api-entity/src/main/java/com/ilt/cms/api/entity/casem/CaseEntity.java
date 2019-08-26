package com.ilt.cms.api.entity.casem;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lippo.cms.util.CMSConstant;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class CaseEntity {

    public enum CaseStatus {
        OPEN, CLOSED;
    }

    private String caseId;
    private String caseNumber;
    private String patientId;
    private String patientNRIC;
    private String patientName;
    private String clinicId;
    private boolean isSingleVisit;
    private List<VisitView> visitIds = new ArrayList<>();
    private List<CoverageView> coverages = new ArrayList<>();
    private SalesOrderEntity salesOrder;
    private PackageEntity purchasedPackage;
    private CaseStatus status;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    private Date createdDate;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VisitView {

        public VisitView(String visitId) {
            this.visitId = visitId;
        }

        @Setter
        private String visitId;
        private String clinicName;
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
        @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
        private LocalDateTime visitDate;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class CoverageView {

        private String planId;
        private String name;
    }
}
