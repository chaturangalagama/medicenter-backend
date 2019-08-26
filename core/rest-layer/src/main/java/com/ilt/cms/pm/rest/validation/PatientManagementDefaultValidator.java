package com.ilt.cms.pm.rest.validation;


import com.ilt.cms.downstream.ClinicDownstream;
import com.ilt.cms.downstream.MedicalCoverageDownstream;
import com.ilt.cms.downstream.PatientDownstream;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PatientManagementDefaultValidator implements Validator {
    private PatientDownstream patientDownstream;
    private ClinicDownstream clinicDownstream;
    private MedicalCoverageDownstream insuranceDownstream;


    public PatientManagementDefaultValidator(PatientDownstream patientDownstream,
                                             ClinicDownstream clinicDownstream, MedicalCoverageDownstream insuranceDownstream) {
        this.patientDownstream = patientDownstream;
        this.clinicDownstream = clinicDownstream;
        this.insuranceDownstream = insuranceDownstream;
    }

    //TODO - #ModuleRestructuring - Patient
    /*public boolean validateInsuranceId(PatientCoverage coverage) {
        String insuranceId = coverage.getMedicalCoverageId();
        return insuranceDownstream
                .doesPlanExists(insuranceId, coverage.getPlanId());

    }*/
}
