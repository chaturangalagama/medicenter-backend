package com.ilt.cms.api.entity.coverage;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ilt.cms.api.entity.common.*;
import com.lippo.cms.util.CMSConstant;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PolicyHolderEntity {
    private String id;
    private UserId identificationNumber;
    private String name;
    private String medicalCoverageId;
    private String planId;
    private String patientCoverageId;
    private String specialRemarks;
    private String costCenter;
    private Address address;
    private ContactNumber mobile;
    private ContactNumber office;
    private ContactNumber home;
    private RelationshipMapping relationship;
    private Status status = Status.ACTIVE;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
    private LocalDate startDate;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern=CMSConstant.JSON_DATE_FORMAT)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
    private LocalDate endDate;



    public static class PolicyHolderCorporate extends PolicyHolderEntity {

    }

    public static class PolicyHolderInsurance extends PolicyHolderEntity {

    }

    public static class PolicyHolderMediSave extends PolicyHolderEntity {

    }

    public static class PolicyHolderChas extends PolicyHolderEntity {

    }
}
