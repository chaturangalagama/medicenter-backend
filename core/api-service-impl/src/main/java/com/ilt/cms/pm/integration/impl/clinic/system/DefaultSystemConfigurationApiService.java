package com.ilt.cms.pm.integration.impl.clinic.system;

import com.ilt.cms.api.entity.common.Relationship;
import com.ilt.cms.api.entity.doctor.SpecialityEntity;
import com.ilt.cms.api.entity.patient.PatientEntity;
import com.ilt.cms.api.payload.DoctorsBySpeciality;
import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.Postcode;
import com.ilt.cms.core.entity.doctor.Doctor;
import com.ilt.cms.core.entity.doctor.Speciality;
import com.ilt.cms.core.entity.system.Instruction;
import com.ilt.cms.core.entity.system.PrescriptionDose;
import com.ilt.cms.downstream.clinic.system.SystemConfigurationApiService;
import com.ilt.cms.pm.business.service.clinic.ClinicService;
import com.ilt.cms.pm.business.service.clinic.DoctorService;
import com.ilt.cms.pm.business.service.clinic.system.PostcodeService;
import com.ilt.cms.pm.business.service.patient.VitalService;
import com.ilt.cms.pm.integration.mapper.clinic.PostcodeMapper;
import com.ilt.cms.repository.clinic.system.InstructionRepository;
import com.ilt.cms.repository.clinic.system.PrescriptionDoseRepository;
import com.lippo.cms.util.IdentificationValidator;
import com.lippo.commons.util.StatusCode;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

@Service
public class DefaultSystemConfigurationApiService implements SystemConfigurationApiService {
    private static final Logger logger = LoggerFactory.getLogger(DefaultSystemConfigurationApiService.class);

    private DoctorService doctorService;
    private ClinicService clinicService;
    private PostcodeService postcodeService;
    private VitalService vitalService;
    private InstructionRepository instructionRepository;
    private PrescriptionDoseRepository prescriptionDoseRepository;

    public DefaultSystemConfigurationApiService(DoctorService doctorService, ClinicService clinicService,
                                                PostcodeService postcodeService, VitalService vitalService,
                                                InstructionRepository instructionRepository,
                                                PrescriptionDoseRepository prescriptionDoseRepository) {
        this.doctorService = doctorService;
        this.clinicService = clinicService;
        this.postcodeService = postcodeService;
        this.vitalService = vitalService;
        this.instructionRepository = instructionRepository;
        this.prescriptionDoseRepository = prescriptionDoseRepository;
    }

    @Override
    public ResponseEntity<ApiResponse> listRelationships() {

        Relationship[] relationships = Relationship.values();
        return httpApiResponse(new HttpApiResponse(relationships));
    }

    @Override
    public ResponseEntity<ApiResponse> listMaritalStatus() {
        PatientEntity.MaritalStatus[] maritalStatuses = PatientEntity.MaritalStatus.values();
        return httpApiResponse(new HttpApiResponse(maritalStatuses));
    }

    @Override
    public ResponseEntity<ApiResponse> listMedicalTestLaboratories() {
        List<String> innovatives = Arrays.asList("Innovative");
        return httpApiResponse(new HttpApiResponse(innovatives));
    }

    @Override
    public ResponseEntity<ApiResponse> listClinicGroups() {
        List<String> serviceResponse = clinicService.listAll().stream()
                .map(Clinic::getGroupName)
                .distinct()
                .collect(Collectors.toList());
        return httpApiResponse(new HttpApiResponse(serviceResponse));
    }

    @Override
    public ResponseEntity<ApiResponse> listInstructions() {
        List<Instruction> instructions = instructionRepository.findAllActive();
        return httpApiResponse(new HttpApiResponse(instructions));
    }

    @Override
    public ResponseEntity<ApiResponse> listPrescriptionDose() {
        List<PrescriptionDose> prescriptionDoses = prescriptionDoseRepository.findAllActive();
        return httpApiResponse(new HttpApiResponse(prescriptionDoses));
    }

    @Override
    public ResponseEntity<ApiResponse> listDefaultLabel() {
        List<String> serviceResponse = Arrays.asList("REFERRAL_LETTER",
                "MEDICAL_CERTIFICATE", "MEDICAL_CHIT", "TIME_CHIT", "OFFICIAL_RECEIPT", "CORPRATE_PAYMENT_RECEIPT", "BILL",
                "BILL_WITH_ADDRESS", "INVOICE", "INVOICE_WITH_ADDRESS", "STATEMENT_OF_ACCOUNT", "PURCHASE_ORDER_FORM",
                "DRUG_LABEL", "PATIENT_LABEL", "PATIENT_LABEL_WITHOUT_ADDRESS", "ADDRESS_LABEL", "VACCINATION_CERT");
        return httpApiResponse(new HttpApiResponse(serviceResponse));
    }

    @Override
    public ResponseEntity<ApiResponse> listDoctorBySpeciality() {

        return httpApiResponse(new HttpApiResponse(listBySpeciality()));
    }

    @Override
    public ResponseEntity<ApiResponse> findPostcode(String code) {
        Postcode postcode = postcodeService.findPostcode(code);
        return httpApiResponse(new HttpApiResponse(PostcodeMapper.mapToEntity(postcode)));
    }

    @Override
    public ResponseEntity<ApiResponse> validateIdentification(String type, String idNumber) {
        switch (type) {
            case "NRIC":
                return httpApiResponse(new HttpApiResponse(IdentificationValidator.isNRICValid(idNumber)));
            case "FIN":
                return httpApiResponse(new HttpApiResponse(IdentificationValidator.isFINValid(idNumber)));
            default:
                return httpApiResponse(new HttpApiResponse(StatusCode.E1002, "Type not recognised [" + type + "]"));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> listVitals() {
        return httpApiResponse(new HttpApiResponse(vitalService.listSupportedVitals()));
    }

    @Override
    public ResponseEntity<ApiResponse> listVitalsDefault() {
        return httpApiResponse(new HttpApiResponse(vitalService.listOfDefaultVitals()));
    }

    private List<DoctorsBySpeciality> listBySpeciality() {
        List<DoctorsBySpeciality> bySpecialities = new ArrayList<>();

        List<Clinic> clinics = clinicService.listAll();
        List<Doctor> doctors = doctorService.findAll();

        for (SpecialityEntity.Practice practice : SpecialityEntity.Practice.values()) {

            List<Map<String, Object>> clinicDetails = new ArrayList<>();

            List<Doctor> docByPractice = doctors
                    .stream()
                    .filter(doctor -> doctor.getSpeciality() == Speciality.Practice.valueOf(practice.name()))
                    .collect(Collectors.toList());

            Set<String> doctorIdsByPractice = docByPractice
                    .stream()
                    .map(PersistedObject::getId)
                    .collect(Collectors.toSet());

            List<Clinic> clinicsByPractice = clinics.stream()
                    .filter(clinic -> clinic.getAttendingDoctorId()
                            .stream()
                            .anyMatch(doctorIdsByPractice::contains))
                    .collect(Collectors.toList());

            for (Clinic clinic : clinicsByPractice) {
                HashMap<String, Object> clinicValues = new HashMap<>();
                clinicValues.put("clinicId", clinic.getId());
                clinicValues.put("clinicCode", clinic.getClinicCode());
                clinicValues.put("doctors", docByPractice.stream()
                        .filter(doctor -> clinic
                                .getAttendingDoctorId()
                                .contains(doctor.getId()))
                        .collect(Collectors.toList()));
                clinicDetails.add(clinicValues);
            }
            bySpecialities.add(new DoctorsBySpeciality(practice, clinicDetails));
        }


        return bySpecialities;
    }
}
