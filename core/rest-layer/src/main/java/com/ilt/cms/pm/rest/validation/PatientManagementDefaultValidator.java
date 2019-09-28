package com.ilt.cms.pm.rest.validation;


import com.ilt.cms.downstream.ClinicDownstream;
import com.ilt.cms.downstream.PatientDownstream;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PatientManagementDefaultValidator implements Validator {
    private PatientDownstream patientDownstream;
    private ClinicDownstream clinicDownstream;


    public PatientManagementDefaultValidator(PatientDownstream patientDownstream,
                                             ClinicDownstream clinicDownstream) {
        this.patientDownstream = patientDownstream;
        this.clinicDownstream = clinicDownstream;
    }

    //TODO - #ModuleRestructuring - Patient
    /*public boolean validateInsuranceId(PatientCoverage coverage) {
        String insuranceId = coverage.getMedicalCoverageId();
        return insuranceDownstream
                .doesPlanExists(insuranceId, coverage.getPlanId());

    }*/
}
