package business.config.repository;

import com.ilt.cms.repository.CustomMedicalCoverageRepository;
import com.ilt.cms.repository.CustomPatientRepository;
import com.ilt.cms.repository.PolicyHolderRepository;
import com.ilt.cms.repository.spring.*;
import com.ilt.cms.repository.spring.calendar.AppointmentRepository;
import com.ilt.cms.repository.spring.calendar.ClinicCalendarRepository;
import com.ilt.cms.repository.spring.calendar.DoctorCalendarRepository;
import com.ilt.cms.repository.spring.consultation.ConsultationFollowupRepository;
import com.ilt.cms.repository.spring.consultation.ConsultationRepository;
import com.ilt.cms.repository.spring.consultation.ConsultationTemplateRepository;
import com.ilt.cms.repository.spring.coverage.MedicalCoverageRepository;
import com.ilt.cms.repository.spring.coverage.PolicyHolderLimitRepository;
import com.ilt.cms.repository.spring.policy.PolicyHolderChasRepository;
import com.ilt.cms.repository.spring.policy.PolicyHolderCorporateRepository;
import com.ilt.cms.repository.spring.policy.PolicyHolderInsuranceRepository;
import com.ilt.cms.repository.spring.policy.PolicyHolderMediSaveRepository;
import com.ilt.cms.repository.spring.system.SystemStoreRepository;
import com.ilt.cms.repository.spring.vaccination.AssociatedCoverageVaccinationRepository;
import com.ilt.cms.repository.spring.vaccination.VaccinationRepository;
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
    public PolicyHolderRepository policyHolderRepository(){
        return new PolicyHolderRepository(mongoTemplate, chasRepository(), corporateRepository(), insuranceRepository(), mediSaveRepository());
    }
    @Bean
    public CustomMedicalCoverageRepository customMedicalCoverageRepository(){
        return new CustomMedicalCoverageRepository(medicalCoverageRepository(), mongoTemplate);
    }
    @Bean
    public PatientRepository patientRepository(){
        return Mockito.mock(PatientRepository.class);
    }
    @Bean
    public MedicalCoverageRepository medicalCoverageRepository(){
        return Mockito.mock(MedicalCoverageRepository.class);
    }
    @Bean
    public CustomPatientRepository customPatientRepository(){
        return new CustomPatientRepository(patientRepository(), mongoTemplate());
    }
    @Bean
    public CustomMedicalCoverageRepository custommedicalCoverageRepository(){
        return new CustomMedicalCoverageRepository(medicalCoverageRepository(), mongoTemplate());
    }
    @Bean
    public ConsultationTemplateRepository consultationTemplateRepository(){
        return Mockito.mock(ConsultationTemplateRepository.class);
    }

    @Bean
    public PolicyHolderChasRepository chasRepository(){
        return Mockito.mock(PolicyHolderChasRepository.class);
    }
    @Bean
    public PolicyHolderCorporateRepository corporateRepository(){
        return Mockito.mock(PolicyHolderCorporateRepository.class);
    }
    @Bean
    public PolicyHolderInsuranceRepository insuranceRepository(){
        return Mockito.mock(PolicyHolderInsuranceRepository.class);
    }
    @Bean
    public PolicyHolderMediSaveRepository mediSaveRepository(){
        return Mockito.mock(PolicyHolderMediSaveRepository.class);
    }
    @Bean
    public DiagnosisRepository diagnosisRepository(){
        return Mockito.mock(DiagnosisRepository.class);
    }

    @Bean
    public AssociatedCoverageVaccinationRepository associatedCoverageVaccinationRepository(){
        return Mockito.mock(AssociatedCoverageVaccinationRepository.class);
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
    public CaseRepository caseRepository() {
        return Mockito.mock(CaseRepository.class);
    }

    @Bean
    public CaseRepositoryImpl caseRepositoryImpl() {
        return Mockito.mock(CaseRepositoryImpl.class);
    }

    @Bean
    public PolicyHolderLimitRepository policyHolderLimitRepository() {
        return Mockito.mock(PolicyHolderLimitRepository.class);
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
    public PatientVisitRegistryRepository patientVisitRegistryRepository() {
        return Mockito.mock(PatientVisitRegistryRepository.class);
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