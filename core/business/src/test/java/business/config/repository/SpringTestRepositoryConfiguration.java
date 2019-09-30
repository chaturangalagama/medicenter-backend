package business.config.repository;

import com.ilt.cms.repository.CustomPatientRepository;
import com.ilt.cms.repository.clinic.*;
import com.ilt.cms.repository.clinic.billing.SalesOrderRepository;
import com.ilt.cms.repository.clinic.system.TemporaryStoreRepository;
import com.ilt.cms.repository.patient.patientVisit.calendar.AppointmentRepository;
import com.ilt.cms.repository.patient.patientVisit.calendar.ClinicCalendarRepository;
import com.ilt.cms.repository.patient.patientVisit.calendar.DoctorCalendarRepository;
import com.ilt.cms.repository.patient.patientVisit.consultation.ConsultationFollowupRepository;
import com.ilt.cms.repository.patient.patientVisit.consultation.ConsultationRepository;
import com.ilt.cms.repository.patient.patientVisit.consultation.ConsultationTemplateRepository;
import com.ilt.cms.repository.clinic.inventory.ItemRepository;
import com.ilt.cms.repository.clinic.inventory.SupplierRepository;
import com.ilt.cms.repository.clinic.system.SystemStoreRepository;
import com.ilt.cms.repository.patient.MedicalAlertRepository;
import com.ilt.cms.repository.patient.PatientRepository;
import com.ilt.cms.repository.patient.VaccinationRepository;
import com.ilt.cms.repository.patient.patientVisit.DiagnosisRepository;
import com.ilt.cms.repository.patient.patientVisit.PatientReferralRepository;
import com.ilt.cms.repository.patient.patientVisit.PatientVisitRepository;
import com.ilt.cms.repository.patient.patientVisit.VisitPurposeRepository;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

@TestConfiguration
public class SpringTestRepositoryConfiguration {

    @MockBean
    public MongoTemplate mongoTemplate;


    public MongoTemplate mongoTemplate(){
        return mongoTemplate;
        //return Mockito.mock(MongoTemplate.class);
    }
    @Bean
    public AllergyGroupRepository allergyGroupRepository(){
        return Mockito.mock(AllergyGroupRepository.class);
    }
    @Bean
    public LabelRepository labelRepository() {
        return Mockito.mock(LabelRepository.class);
    }
    @Bean
    public ClinicRepository clinicRepository(){

        return Mockito.mock(ClinicRepository.class);
    }
    @Bean
    public DoctorRepository doctorRepository(){
        return Mockito.mock(DoctorRepository.class);
    }
    @Bean
    public PatientRepository patientRepository(){
        return Mockito.mock(PatientRepository.class);
    }
    @Bean
    public CustomPatientRepository customPatientRepository(){
        return new CustomPatientRepository(patientRepository(), mongoTemplate());
    }
    @Bean
    public ConsultationTemplateRepository consultationTemplateRepository(){
        return Mockito.mock(ConsultationTemplateRepository.class);
    }

    @Bean
    public DiagnosisRepository diagnosisRepository(){
        return Mockito.mock(DiagnosisRepository.class);
    }
    @Bean
    public SupplierRepository supplierRepository(){
        return Mockito.mock(SupplierRepository.class);
    }
    @Bean
    public MedicalAlertRepository medicalAlertRepository(){
        return Mockito.mock(MedicalAlertRepository.class);
    }
    @Bean
    public NotificationRepository notificationRepository(){
        return Mockito.mock(NotificationRepository.class);
    }
    @Bean
    public PatientNoteRepository patientNoteRepository(){
        return Mockito.mock(PatientNoteRepository.class);
    }
    @Bean
    public PostcodeRepository postcodeRepository(){
        return Mockito.mock(PostcodeRepository.class);
    }
    @Bean
    public TemporaryStoreRepository temporaryStoreRepository(){
        return Mockito.mock(TemporaryStoreRepository.class);
    }
    @Bean
    public VaccinationRepository vaccinationRepository(){
        return Mockito.mock(VaccinationRepository.class);
    }
    @Bean
    public VisitPurposeRepository visitPurposeRepository(){
        return Mockito.mock(VisitPurposeRepository.class);
    }
    @Bean
    public MedicalServiceRepository medicalServiceRepository(){
        return Mockito.mock(MedicalServiceRepository.class);
    }

    @Bean
    public AppointmentRepository appointmentRepository(){
        return Mockito.mock(AppointmentRepository.class);
    }

    @Bean
    public ConsultationRepository consultationRepository() {
        return Mockito.mock(ConsultationRepository.class);
    }

    @Bean
    public ConsultationFollowupRepository consultationFollowupRepository() {
        return Mockito.mock(ConsultationFollowupRepository.class);
    }

    @Bean
    public PatientReferralRepository patientReferralRepository() {
        return Mockito.mock(PatientReferralRepository.class);
    }

    @Bean
    public ItemRepository itemRepository() {
        return Mockito.mock(ItemRepository.class);
    }


    @Bean
    public SystemStoreRepository systemStoreRepository() {
        return Mockito.mock(SystemStoreRepository.class);
    }

    @Bean
    public PatientVisitRepository patientVisitRegistryRepository() {
        return Mockito.mock(PatientVisitRepository.class);
    }

    @Bean
    public ClinicCalendarRepository clinicCalendarRepository(){
        return Mockito.mock(ClinicCalendarRepository.class);
    }

    @Bean
    public DoctorCalendarRepository doctorCalendarRepository(){
        return Mockito.mock(DoctorCalendarRepository.class);
    }

    @Bean
    public SalesOrderRepository salesOrderRepository() {
        return Mockito.mock(SalesOrderRepository.class);
    }
}
