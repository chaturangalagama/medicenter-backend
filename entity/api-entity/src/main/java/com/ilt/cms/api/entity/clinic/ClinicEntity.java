package com.ilt.cms.api.entity.clinic;

import com.ilt.cms.api.entity.common.CorporateAddress;
import com.ilt.cms.api.entity.common.Status;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ClinicEntity {
    private String id;
    private String name;
    private String groupName;
    private String emailAddress;
    private CorporateAddress address;
    private String faxNumber;
    private String contactNumber;
    private Status status;
    private String companyRegistrationNumber;
    private String gstRegistrationNumber;
    private String _migrationSyncId;
    private String clinicCode;
    private List<String> attendingDoctorId = new ArrayList<>();
    private List<String> clinicStaffUsernames = new ArrayList<>();
}
