package com.ilt.cms.pm.integration.mapper;

import com.ilt.cms.api.entity.consultation.MedicalCertificate;
import com.ilt.cms.api.entity.consultation.PatientReferral;
import com.ilt.cms.api.entity.doctor.SpecialityEntity;
import com.ilt.cms.api.entity.medical.DispatchItemEntity;
import com.ilt.cms.api.entity.medical.MedicalReferenceEntity;
import com.ilt.cms.core.entity.doctor.Speciality;
import com.ilt.cms.core.entity.medical.DispatchItem;
import com.ilt.cms.core.entity.medical.MedicalReference;

import java.util.function.Function;
import java.util.stream.Collectors;

public class MedicalReferenceMapper {

    public static MedicalReferenceEntity mapToEntity(MedicalReference medicalReference) {
        if (medicalReference == null) {
            return null;
        }

        MedicalReferenceEntity referenceEntity = new MedicalReferenceEntity();
        if (medicalReference.getConsultation() != null) {
            referenceEntity.setConsultation(ConsultationMapper.mapToEntity(medicalReference.getConsultation()));
        }
        if (medicalReference.getDiagnosisIds() != null) {
            referenceEntity.setDiagnosisIds(medicalReference.getDiagnosisIds());
        }
        if (medicalReference.getMedicalCertificates() != null) {
            referenceEntity.setMedicalCertificates(medicalReference.getMedicalCertificates().stream()
                    .map(mapMedicalCertificatesToEntity()).collect(Collectors.toList()));
        }
        if (medicalReference.getPatientReferral() != null) {
            referenceEntity.setPatientReferral(mapPatientReferralToEntity(medicalReference.getPatientReferral()));
        }
        if (medicalReference.getConsultationFollowup() != null) {
            referenceEntity.setConsultationFollowup(ConsultationFollowUpMapper.mapToEntity(medicalReference.getConsultationFollowup()));
        }
        if (medicalReference.getDispatchItems() != null) {
            referenceEntity.setDispatchItemEntities(medicalReference.getDispatchItems().stream()
                    .map(mapDispatchItemToEntity()).collect(Collectors.toList()));
        }
        return referenceEntity;
    }

    public static MedicalReference mapToCore(MedicalReferenceEntity medicalReferenceEntity) {
        if (medicalReferenceEntity == null) {
            return null;
        }

        MedicalReference medicalReference = new MedicalReference();
        if (medicalReferenceEntity.getConsultation() != null) {
            medicalReference.setConsultation(ConsultationMapper.mapToCore(medicalReferenceEntity.getConsultation()));
        }
        if (medicalReferenceEntity.getDiagnosisIds() != null) {
            medicalReference.setDiagnosisIds(medicalReferenceEntity.getDiagnosisIds());
        }
        if (medicalReferenceEntity.getMedicalCertificates() != null) {
            medicalReference.setMedicalCertificates(medicalReferenceEntity.getMedicalCertificates().stream()
                    .map(mapMedicalCertificatesToCore()).collect(Collectors.toList()));
        }
        if (medicalReferenceEntity.getPatientReferral() != null) {
            medicalReference.setPatientReferral(mapPatientReferralToCore(medicalReferenceEntity.getPatientReferral()));
        }
        if (medicalReferenceEntity.getConsultationFollowup() != null) {
            medicalReference.setConsultationFollowup(ConsultationFollowUpMapper.mapToCore(medicalReferenceEntity.getConsultationFollowup()));
        }
        if (medicalReferenceEntity.getDispatchItemEntities() != null) {
            medicalReference.setDispatchItems(medicalReferenceEntity.getDispatchItemEntities().stream()
                    .map(mapDispatchItemEntityToCore()).collect(Collectors.toList()));
        }
        return medicalReference;
    }

    public static PatientReferral mapPatientReferralToEntity(com.ilt.cms.core.entity.consultation.PatientReferral patientReferral) {
        if (patientReferral == null) {
            return null;
        }

        PatientReferral referral = new PatientReferral();
        referral.setReferralId(patientReferral.getId());
        if (patientReferral.getPatientReferrals() != null) {
            referral.setPatientReferrals(patientReferral.getPatientReferrals().stream()
                    .map(mapPatientReferralDetailsToEntity()).collect(Collectors.toList()));
        }
        return referral;
    }

    public static com.ilt.cms.core.entity.consultation.PatientReferral mapPatientReferralToCore(PatientReferral patientReferral) {
        if (patientReferral == null) {
            return null;
        }
        com.ilt.cms.core.entity.consultation.PatientReferral referral = new com.ilt.cms.core.entity.consultation.PatientReferral();
        referral.setId(patientReferral.getReferralId());
        referral.setPatientReferrals(patientReferral.getPatientReferrals().stream()
                .map(mapPatientReferralDetailsToCore()).collect(Collectors.toList()));
        return referral;
    }

    private static Function<com.ilt.cms.core.entity.consultation.MedicalCertificate, MedicalCertificate> mapMedicalCertificatesToEntity() {
        return medicalCertificate ->
        {
            MedicalCertificate.Half halfDayOption = null;
            if (medicalCertificate.getHalfDayOption() != null) {
                halfDayOption = MedicalCertificate.Half.valueOf(medicalCertificate.getHalfDayOption().name());
            }

            return new MedicalCertificate(medicalCertificate.getPurpose(),
                    medicalCertificate.getStartDate(),
                    medicalCertificate.getNumberOfDays(),
                    medicalCertificate.getReferenceNumber(),
                    medicalCertificate.getRemark(),
                    halfDayOption);
        };
    }

    public static Function<MedicalCertificate, com.ilt.cms.core.entity.consultation.MedicalCertificate> mapMedicalCertificatesToCore() {
        return medicalCertificate -> {
            com.ilt.cms.core.entity.consultation.MedicalCertificate.Half halfDayOption = null;
            if (medicalCertificate.getHalfDayOption() != null) {
                halfDayOption = com.ilt.cms.core.entity.consultation.MedicalCertificate.Half.valueOf(medicalCertificate.getHalfDayOption().name());
            }
            return new com.ilt.cms.core.entity.consultation.MedicalCertificate(medicalCertificate.getPurpose(),
                    medicalCertificate.getStartDate(),
                    medicalCertificate.getNumberOfDays(),
                    medicalCertificate.getReferenceNumber(),
                    medicalCertificate.getRemark(),
                    halfDayOption);
        };
    }

    private static Function<com.ilt.cms.core.entity.consultation.PatientReferral.PatientReferralDetails, PatientReferral.PatientReferralDetails> mapPatientReferralDetailsToEntity() {
        return referralDetails ->
        {
            if (referralDetails.isExternalReferral()) {
                return new PatientReferral.PatientReferralDetails(SpecialityEntity.Practice.valueOf(referralDetails.getPractice().name()),
                        referralDetails.isExternalReferral(),
                        new PatientReferral.ExternalReferralDetails(referralDetails.getExternalReferralDetails().getDoctorName(),
                                referralDetails.getExternalReferralDetails().getAddress(), referralDetails.getExternalReferralDetails().getPhoneNumber()),
                        referralDetails.getClinicId(),
                        referralDetails.getDoctorId(),
                        referralDetails.getAppointmentDateTime(),
                        referralDetails.getMemo());
            } else {
                return new PatientReferral.PatientReferralDetails(SpecialityEntity.Practice.valueOf(referralDetails.getPractice().name()),
                        referralDetails.isExternalReferral(),
                        new PatientReferral.ExternalReferralDetails(),
                        referralDetails.getClinicId(),
                        referralDetails.getDoctorId(),
                        referralDetails.getAppointmentDateTime(),
                        referralDetails.getMemo());
            }
        };
    }

    private static Function<PatientReferral.PatientReferralDetails, com.ilt.cms.core.entity.consultation.PatientReferral.PatientReferralDetails> mapPatientReferralDetailsToCore() {
        return referralDetails -> {
            if (referralDetails.isExternalReferral()) {
                return new com.ilt.cms.core.entity.consultation.PatientReferral.PatientReferralDetails(Speciality.Practice.valueOf(referralDetails.getPractice().name()),
                        referralDetails.isExternalReferral(),
                        new com.ilt.cms.core.entity.consultation.PatientReferral.ExternalReferralDetails(referralDetails.getExternalReferralDetails().getDoctorName(),
                                referralDetails.getExternalReferralDetails().getAddress(),
                                referralDetails.getExternalReferralDetails().getPhoneNumber()),
                        referralDetails.getClinicId(),
                        referralDetails.getDoctorId(),
                        referralDetails.getAppointmentDateTime(),
                        referralDetails.getMemo());
            } else {
                return new com.ilt.cms.core.entity.consultation.PatientReferral.PatientReferralDetails(Speciality.Practice.valueOf(referralDetails.getPractice().name()),
                        false,
                        null,
                        referralDetails.getClinicId(),
                        referralDetails.getDoctorId(),
                        referralDetails.getAppointmentDateTime(),
                        referralDetails.getMemo());

            }
        };
    }

    private static Function<DispatchItem, DispatchItemEntity> mapDispatchItemToEntity() {
        return dispatchItem ->
                new DispatchItemEntity(dispatchItem.getPurchasedId(),
                        dispatchItem.getItemId(),
                        dispatchItem.getDosageUom(),
                        dispatchItem.getInstruct(),
                        dispatchItem.getDuration(),
                        dispatchItem.getDosage(),
                        dispatchItem.getQuantity(),
                        dispatchItem.getOriTotalPrice(),
                        dispatchItem.getBatchNo(),
                        dispatchItem.getExpiryDate(),
                        SalesOrderMapper.mapToEntity(dispatchItem.getItemPriceAdjustment()),
                        dispatchItem.getRemarks(),
                        dispatchItem.getDosageInstruction());
    }

    private static Function<DispatchItemEntity, DispatchItem> mapDispatchItemEntityToCore() {
        return dispatchItemEntity ->
                new DispatchItem(dispatchItemEntity.getPurchasedId(),
                        dispatchItemEntity.getItemId(),
                        dispatchItemEntity.getDosageUom(),
                        dispatchItemEntity.getInstruct(),
                        dispatchItemEntity.getDuration(),
                        dispatchItemEntity.getDosage(),
                        dispatchItemEntity.getQuantity(),
                        dispatchItemEntity.getOriTotalPrice(),
                        dispatchItemEntity.getBatchNo(),
                        dispatchItemEntity.getExpiryDate(),
                        SalesOrderMapper.mapToCore(dispatchItemEntity.getItemPriceAdjustment()),
                        dispatchItemEntity.getRemarks(),
                        dispatchItemEntity.getDosageInstruction());
    }

    public static DispatchItemEntity mapDispatchItemToEntity(DispatchItem dispatchItem) {
        return new DispatchItemEntity(dispatchItem.getPurchasedId(),
                dispatchItem.getItemId(),
                dispatchItem.getDosageUom(),
                dispatchItem.getInstruct(),
                dispatchItem.getDuration(),
                dispatchItem.getDosage(),
                dispatchItem.getQuantity(),
                dispatchItem.getOriTotalPrice(),
                dispatchItem.getBatchNo(),
                dispatchItem.getExpiryDate(),
                SalesOrderMapper.mapToEntity(dispatchItem.getItemPriceAdjustment()),
                dispatchItem.getRemarks(),
                dispatchItem.getDosageInstruction());
    }

    public static DispatchItem mapDispatchItemEntityToCore(DispatchItemEntity dispatchItemEntity) {
        return new DispatchItem(dispatchItemEntity.getPurchasedId(),
                dispatchItemEntity.getItemId(),
                dispatchItemEntity.getDosageUom(),
                dispatchItemEntity.getInstruct(),
                dispatchItemEntity.getDuration(),
                dispatchItemEntity.getDosage(),
                dispatchItemEntity.getQuantity(),
                dispatchItemEntity.getOriTotalPrice(),
                dispatchItemEntity.getBatchNo(),
                dispatchItemEntity.getExpiryDate(),
                SalesOrderMapper.mapToCore(dispatchItemEntity.getItemPriceAdjustment()),
                dispatchItemEntity.getRemarks(),
                dispatchItemEntity.getDosageInstruction());
    }
}
