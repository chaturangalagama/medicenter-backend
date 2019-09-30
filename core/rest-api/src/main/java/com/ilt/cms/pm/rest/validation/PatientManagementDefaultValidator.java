package com.ilt.cms.pm.rest.validation;


import com.ilt.cms.downstream.clinic.ClinicApiService;
import com.ilt.cms.downstream.patient.PatientApiService;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class PatientManagementDefaultValidator implements Validator {
    private PatientApiService patientApiService;
    private ClinicApiService clinicApiService;


    public PatientManagementDefaultValidator(PatientApiService patientApiService,
                                             ClinicApiService clinicApiService) {
        this.patientApiService = patientApiService;
        this.clinicApiService = clinicApiService;
    }

    //TODO - #ModuleRestructuring - Patient
    /*public boolean validateInsuranceId(PatientCoverage coverage) {
        String insuranceId = coverage.getMedicalCoverageId();
        return insuranceDownstream
                .doesPlanExists(insuranceId, coverage.getPlanId());

    }*/
}
