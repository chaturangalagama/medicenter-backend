package com.ilt.cms.core.entity.consultation;

import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.doctor.Speciality;
import com.lippo.commons.util.CommonUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.lippo.commons.util.CommonUtils.isStringValid;

public class PatientReferral extends PersistedObject {

    private List<PatientReferralDetails> patientReferrals = new ArrayList<>();

    public List<PatientReferralDetails> getPatientReferrals() {
        if (patientReferrals == null) patientReferrals = new ArrayList<>();
        return patientReferrals;
    }

    public PatientReferral copy(PatientReferral referral) {
        patientReferrals = new ArrayList<>(referral.patientReferrals);
        return this;
    }

    public boolean areParametersValid() {
        return patientReferrals.stream().allMatch(PatientReferralDetails::areParametersValid);
    }

    public void setPatientReferrals(List<PatientReferralDetails> patientReferrals) {
        this.patientReferrals = patientReferrals;
    }

    public static class ExternalReferralDetails {
        private String doctorName;
        private String address;
        private String phoneNumber;

        public ExternalReferralDetails() {
        }

        public ExternalReferralDetails(String doctorName, String address, String phoneNumber) {
            this.doctorName = doctorName;
            this.address = address;
            this.phoneNumber = phoneNumber;
        }


        public boolean areParametersValid() {
            return CommonUtils.isStringValid(doctorName);
        }

        public String getDoctorName() {
            return doctorName;
        }

        public String getAddress() {
            return address;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        @Override
        public String toString() {
            return "ExternalReferralDetails{" +
                    "doctorName='" + doctorName + '\'' +
                    ", address='" + address + '\'' +
                    ", phoneNumber='" + phoneNumber + '\'' +
                    '}';
        }
    }

    public static class PatientReferralDetails {
        private Speciality.Practice practice;
        private boolean externalReferral;
        private ExternalReferralDetails externalReferralDetails;
        private String clinicId;
        private String doctorId;
        private LocalDateTime appointmentDateTime;
        private String memo;

        public PatientReferralDetails() {
        }

        public PatientReferralDetails(Speciality.Practice practice, boolean externalReferral, ExternalReferralDetails externalReferralDetails, String clinicId, String doctorId, LocalDateTime appointmentDateTime, String memo) {
            this.practice = practice;
            this.externalReferral = externalReferral;
            this.externalReferralDetails = externalReferralDetails;
            this.clinicId = clinicId;
            this.doctorId = doctorId;
            this.appointmentDateTime = appointmentDateTime;
            this.memo = memo;
        }

        public boolean areParametersValid() {
            if (!externalReferral) {
                return practice != null
                        && isStringValid(clinicId)
                        && isStringValid(doctorId);
            } else {
                return practice != null
                        && externalReferralDetails != null
                        && externalReferralDetails.areParametersValid();
            }
        }

        public Speciality.Practice getPractice() {
            return practice;
        }

        public void setPractice(Speciality.Practice practice) {
            this.practice = practice;
        }

        public String getClinicId() {
            return clinicId;
        }

        public void setClinicId(String clinicId) {
            this.clinicId = clinicId;
        }

        public String getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(String doctorId) {
            this.doctorId = doctorId;
        }

        public LocalDateTime getAppointmentDateTime() {
            return appointmentDateTime;
        }

        public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
            this.appointmentDateTime = appointmentDateTime;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public boolean isExternalReferral() {
            return externalReferral;
        }

        public void setExternalReferral(boolean externalReferral) {
            this.externalReferral = externalReferral;
        }

        public ExternalReferralDetails getExternalReferralDetails() {
            return externalReferralDetails;
        }

        public void setExternalReferralDetails(ExternalReferralDetails externalReferralDetails) {
            this.externalReferralDetails = externalReferralDetails;
        }

        @Override
        public String toString() {
            return "PatientReferralDetails{" +
                    "practice=" + practice +
                    ", externalReferral=" + externalReferral +
                    ", externalReferralDetails=" + externalReferralDetails +
                    ", clinicId='" + clinicId + '\'' +
                    ", doctorId='" + doctorId + '\'' +
                    ", appointmentDateTime=" + appointmentDateTime +
                    ", memo='" + memo + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PatientReferral{" +
                "patientReferrals=" + patientReferrals +
                '}';
    }
}
