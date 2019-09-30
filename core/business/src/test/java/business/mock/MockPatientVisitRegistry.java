package business.mock;

import com.ilt.cms.core.entity.sales.ItemPriceAdjustment;
import com.ilt.cms.core.entity.consultation.Consultation;
import com.ilt.cms.core.entity.consultation.MedicalCertificate;
import com.ilt.cms.core.entity.consultation.PatientReferral;
import com.ilt.cms.core.entity.doctor.Speciality;
import com.ilt.cms.core.entity.medical.DispatchItem;
import com.ilt.cms.core.entity.medical.MedicalReference;
import com.ilt.cms.core.entity.reminder.ReminderStatus;
import com.ilt.cms.core.entity.visit.ConsultationFollowup;
import com.ilt.cms.core.entity.visit.PatientVisitRegistry;
import com.ilt.cms.core.entity.visit.Priority;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

public class MockPatientVisitRegistry {

    public static PatientVisitRegistry mockVisitRegistry() {
        PatientVisitRegistry registry = new PatientVisitRegistry();
        registry.setId("1245842");
        registry.setVisitNumber("000001");
        registry.setPreferredDoctorId("DOC0001");
        registry.setPatientId("P0001");
        registry.setClinicId("CLI1000");
        registry.setVisitPurpose("Consultation");
        registry.setPriority(Priority.NORMAL);
        registry.setEndTime(LocalDateTime.now());
        registry.setStartTime(LocalDateTime.now());
        registry.setVisitStatus(PatientVisitRegistry.PatientVisitState.INITIAL);
        registry.setMedicalReference(mockMedicalReference());
        registry.setPatientQueue(new PatientVisitRegistry.PatientQueue(10000, true));
        return registry;
    }

    public static PatientVisitRegistry mockVisitRegistryTwo() {
        PatientVisitRegistry registry = new PatientVisitRegistry();
        registry.setId("1245842");
        registry.setVisitNumber("000001");
        registry.setPreferredDoctorId("DOC0001");
        registry.setPatientId("P0001");
        registry.setClinicId("CLI1000");
        registry.setVisitPurpose("Consultation");
        registry.setPriority(Priority.NORMAL);
        registry.setEndTime(LocalDateTime.now());
        registry.setStartTime(LocalDateTime.now());
        registry.setVisitStatus(PatientVisitRegistry.PatientVisitState.INITIAL);
        registry.setMedicalReference(mockMedicalReferenceTwo());
        registry.setPatientQueue(new PatientVisitRegistry.PatientQueue(10000, true));
        return registry;
    }

    public static MedicalReference mockMedicalReference() {
        MedicalReference reference = new MedicalReference();
        reference.setDiagnosisIds(Arrays.asList("DIG0001"));
        reference.setMedicalCertificates(Arrays.asList(mockMedicalCertificate()));
        reference.setConsultationFollowup(mockConsultationFollowup());
        reference.setPatientReferral(mockPatientReferral());
        reference.setConsultation(mockConsultation());
        reference.setDispatchItems(Arrays.asList(mockDispatchItemOne()));

        return reference;
    }

    public static MedicalReference mockMedicalReferenceTwo() {
        MedicalReference reference = new MedicalReference();
        reference.setDiagnosisIds(Arrays.asList("DIG0001"));
        reference.setMedicalCertificates(Arrays.asList(mockMedicalCertificate()));
        reference.setConsultationFollowup(mockConsultationFollowup());
        reference.setPatientReferral(mockPatientReferral());
        reference.setConsultation(mockConsultation());
        reference.setDispatchItems(Arrays.asList(mockDispatchItemTwo()));

        return reference;
    }

    public static MedicalCertificate mockMedicalCertificate(){
        MedicalCertificate certificate = new MedicalCertificate();
        certificate.setReferenceNumber("MCREF001");
        certificate.setPurpose("Hospitalized");
        certificate.setHalfDayOption(MedicalCertificate.Half.AM_LAST);
        certificate.setNumberOfDays(2);
        certificate.setRemark("Urgent case");
        certificate.setStartDate(LocalDate.now());
        return certificate;
    }

    public static PatientReferral mockPatientReferral(){
        PatientReferral.PatientReferralDetails referralDetails = new PatientReferral.PatientReferralDetails();
        referralDetails.setClinicId("CLI0001");
        referralDetails.setDoctorId("DOC0002");
        referralDetails.setAppointmentDateTime(LocalDateTime.now());
        referralDetails.setExternalReferral(true);
        referralDetails.setMemo("Patient referral memo");
        referralDetails.setPractice(Speciality.Practice.ALLERGY);
        referralDetails.setExternalReferralDetails(new PatientReferral.ExternalReferralDetails("Jhon Clark", "319455 Toa Payoh, Singapore", "93749138"));

        PatientReferral referral = new PatientReferral();
        referral.setId("544854");
        referral.setPatientReferrals(Arrays.asList(referralDetails));
        return referral;
    }

    public static ConsultationFollowup mockConsultationFollowup(){
        ConsultationFollowup followUp = new ConsultationFollowup();
        followUp.setId("54545");
        followUp.setReminderStatus(new ReminderStatus(false, LocalDateTime.now(), false, "Need to remind", "EREF0002"));
        followUp.setRemarks("Consultation follow up remark");
        followUp.setPatientVisitId("V0002");
        followUp.setPatientId("P0001");
        followUp.setFollowupDate(LocalDate.now());
        followUp.setDoctorId("DOC0002");
        followUp.setClinicId("CLI0001");
        return followUp;
    }

    public static Consultation mockConsultation(){
        Consultation consultation = new Consultation();
        consultation.setId("456721");
        consultation.setPatientId("P0001");
        consultation.setDoctorId("DOC0004");
        consultation.setClinicId("CLI0002");
        consultation.setMemo("Consultation memo");
        consultation.setConsultationStartTime(LocalDateTime.now());
        consultation.setConsultationEndTime(LocalDateTime.now());
        consultation.setConsultationNotes("Consultation note");
        consultation.setClinicNotes("Clinic note");
        return consultation;
    }

    public static DispatchItem mockDispatchItemOne(){
        DispatchItem dispatchItem = new DispatchItem();
        dispatchItem.setItemId("ITEM001");
        dispatchItem.setBatchNo("012458");
        dispatchItem.setDosage(1);
        dispatchItem.setDosageUom("TAB");
        dispatchItem.setDuration(3);
        dispatchItem.setExpiryDate(LocalDate.now());
        dispatchItem.setInstruct("TDS");
        dispatchItem.setQuantity(20);
        dispatchItem.setRemarks("Remarks for dispatch item");
        dispatchItem.setOriTotalPrice(450);
        dispatchItem.setItemPriceAdjustment( new ItemPriceAdjustment(12, ItemPriceAdjustment.PaymentType.DOLLAR, "remark"));
        return dispatchItem;
    }

    public static DispatchItem mockDispatchItemTwo(){
        DispatchItem dispatchItem = new DispatchItem();
        dispatchItem.setItemId("ITEM001");
        dispatchItem.setBatchNo("012458");
        dispatchItem.setDosage(1);
        dispatchItem.setDosageUom("TAB");
        dispatchItem.setDuration(3);
        dispatchItem.setExpiryDate(LocalDate.now());
        dispatchItem.setInstruct("TDS");
        dispatchItem.setQuantity(20);
        dispatchItem.setRemarks("Remarks for dispatch item");
        dispatchItem.setOriTotalPrice(400);
        dispatchItem.setItemPriceAdjustment( new ItemPriceAdjustment(17, ItemPriceAdjustment.PaymentType.DOLLAR, "remark"));
        return dispatchItem;
    }
}
