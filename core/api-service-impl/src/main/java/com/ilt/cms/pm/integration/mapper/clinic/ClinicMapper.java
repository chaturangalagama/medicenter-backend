package com.ilt.cms.pm.integration.mapper.clinic;

import com.ilt.cms.api.entity.clinic.ClinicEntity;
import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.core.entity.common.CorporateAddress;

public class ClinicMapper {
    public static Clinic mapToCore(ClinicEntity clinicEntity) {
        if(clinicEntity == null){
            return null;
        }
        Clinic clinic = new Clinic();
        clinic.setId(clinicEntity.getId());
        com.ilt.cms.api.entity.common.CorporateAddress corporateAddressEntity = clinicEntity.getAddress();
        if(corporateAddressEntity != null) {
            CorporateAddress corporateAddress =
                    new CorporateAddress(corporateAddressEntity.getAttentionTo(), corporateAddressEntity.getAddress(), corporateAddressEntity.getPostalCode());
            clinic.setAddress(corporateAddress);
        }

        clinic.setAttendingDoctorId(clinicEntity.getAttendingDoctorId());
        clinic.setClinicCode(clinicEntity.getClinicCode());
        clinic.setClinicStaffUsernames(clinicEntity.getClinicStaffUsernames());
        clinic.setCompanyRegistrationNumber(clinicEntity.getCompanyRegistrationNumber());
        clinic.setContactNumber(clinicEntity.getContactNumber());
        clinic.setEmailAddress(clinicEntity.getEmailAddress());
        clinic.setFaxNumber(clinicEntity.getFaxNumber());
        clinic.setGroupName(clinicEntity.getGroupName());
        clinic.setGstRegistrationNumber(clinicEntity.getGstRegistrationNumber());
        clinic.setName(clinicEntity.getName());

        return clinic;
    }

    public static ClinicEntity mapToEntity(Clinic clinic) {
        if(clinic == null){
            return null;
        }
        ClinicEntity clinicEntity = new ClinicEntity();
        clinicEntity.setId(clinic.getId());
        CorporateAddress corporateAddress = clinic.getAddress();
        if(corporateAddress != null) {
            com.ilt.cms.api.entity.common.CorporateAddress corporateAddressEntity =
                    new com.ilt.cms.api.entity.common.CorporateAddress(corporateAddress.getAttentionTo(), corporateAddress.getAddress(), corporateAddress.getPostalCode());
            clinicEntity.setAddress(corporateAddressEntity);
        }

        clinicEntity.setAttendingDoctorId(clinic.getAttendingDoctorId());
        clinicEntity.setClinicCode(clinic.getClinicCode());
        clinicEntity.setClinicStaffUsernames(clinic.getClinicStaffUsernames());
        clinicEntity.setCompanyRegistrationNumber(clinic.getCompanyRegistrationNumber());
        clinicEntity.setContactNumber(clinic.getContactNumber());
        clinicEntity.setEmailAddress(clinic.getEmailAddress());
        clinicEntity.setFaxNumber(clinic.getFaxNumber());
        clinicEntity.setGroupName(clinic.getGroupName());
        clinicEntity.setGstRegistrationNumber(clinic.getGstRegistrationNumber());
        clinicEntity.setName(clinic.getName());

        return clinicEntity;
    }
}
