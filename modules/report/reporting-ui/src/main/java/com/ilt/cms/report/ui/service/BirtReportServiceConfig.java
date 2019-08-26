package com.ilt.cms.report.ui.service;

import com.ilt.cms.database.casem.CaseDatabaseService;
import com.ilt.cms.database.clinic.ClinicDatabaseService;
import com.ilt.cms.database.consultation.ConsultationDatabaseService;
import com.ilt.cms.database.coverage.MedicalCoverageDatabaseService;
import com.ilt.cms.database.diagnosis.DiagnosisDatabaseService;
import com.ilt.cms.database.doctor.DoctorDatabaseService;
import com.ilt.cms.database.item.ItemDatabaseService;
import com.ilt.cms.database.patient.PatientDatabaseService;
import com.ilt.cms.database.visit.PatientReferralDatabaseService;
import com.ilt.cms.database.visit.PatientVisitRegistryDatabaseService;
import com.ilt.cms.db.service.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class BirtReportServiceConfig implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public PatientVisitRegistryDatabaseService patientVisitRegistryDatabaseService;
    public PatientDatabaseService patientDatabaseService;
    public DoctorDatabaseService doctorDatabaseService;
    public CaseDatabaseService caseDatabaseService;
    public PatientReferralDatabaseService patientReferralDatabaseService;
    public ClinicDatabaseService clinicDatabaseService;
    public DiagnosisDatabaseService diagnosisDatabaseService;
    public ConsultationDatabaseService consultationDatabaseService;
    public ItemDatabaseService itemDatabaseService;
    public MedicalCoverageDatabaseService medicalCoverageDatabaseService;

    public BirtReportServiceConfig() {
        if (applicationContext != null) {
            patientVisitRegistryDatabaseService = applicationContext.getBean(MongoPatientVisitRegistryDatabaseService.class);
            patientDatabaseService = applicationContext.getBean(MongoPatientDatabaseService.class);
            doctorDatabaseService = applicationContext.getBean(MongoDoctorDatabaseService.class);
            caseDatabaseService = applicationContext.getBean(MongoCaseDatabaseService.class);
            patientReferralDatabaseService = applicationContext.getBean(MongoPatientReferralDatabaseService.class);
            clinicDatabaseService = applicationContext.getBean(MongoClinicDatabaseService.class);
            diagnosisDatabaseService = applicationContext.getBean(MongoDiagnosisDatabaseService.class);
            consultationDatabaseService = applicationContext.getBean(MongoConsultationDatabaseService.class);
            itemDatabaseService = applicationContext.getBean(MongoItemDatabaseService.class);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BirtReportServiceConfig.applicationContext = applicationContext;
    }
}
