package com.ilt.cms.pm.integration.impl.patient;

import com.ilt.cms.api.entity.patient.PatientVaccination;
import com.ilt.cms.api.entity.vaccination.VaccinationEntity;
import com.ilt.cms.core.entity.vaccination.Vaccination;
import com.ilt.cms.downstream.patient.VaccinationApiService;
import com.ilt.cms.pm.business.service.patient.VaccinationService;
import com.ilt.cms.pm.integration.mapper.patient.PatientMapper;
import com.ilt.cms.pm.integration.mapper.patient.VaccinationMapper;
import com.lippo.cms.exception.CMSException;
import com.lippo.cms.util.CMSConstant;
import com.lippo.commons.util.StatusCode;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

@Service
public class DefaultVaccinationApiService implements VaccinationApiService {
    private static final Logger logger = LoggerFactory.getLogger(DefaultVaccinationApiService.class);

    private VaccinationService vaccinationService;

    public DefaultVaccinationApiService(VaccinationService vaccinationService){
        this.vaccinationService = vaccinationService;
    }
    @Override
    public ResponseEntity<ApiResponse> listVaccines(int page, int size) {
        HashMap<String, Object> vaccinesMap = vaccinationService.listVaccines(page, size);
        List<Vaccination> vaccinations = (List<Vaccination>) vaccinesMap.get(CMSConstant.PAYLOAD_KEY_CONTENT);
        List<VaccinationEntity> vaccinationEntities = vaccinations.stream().map(VaccinationMapper::mapToEntity).collect(Collectors.toList());

        vaccinesMap.put(CMSConstant.PAYLOAD_KEY_CONTENT, vaccinationEntities);
        return httpApiResponse(new HttpApiResponse(vaccinesMap));
    }

    @Override
    public ResponseEntity<ApiResponse> listAllVaccines() {
        List<Vaccination> vaccinations = vaccinationService.listAllVaccines();
        List<VaccinationEntity> vaccinationEntities = vaccinations.stream().map(VaccinationMapper::mapToEntity).collect(Collectors.toList());
        return httpApiResponse(new HttpApiResponse(vaccinationEntities));
    }

    @Override
    public ResponseEntity<ApiResponse> removeVaccinationFromPatient(String patientId, String associationVaccinationId) {
        try {
            boolean success = vaccinationService.removeVaccinationFromPatient(patientId, associationVaccinationId);
            return httpApiResponse(new HttpApiResponse(StatusCode.S0000));
        } catch (CMSException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }

    }

    @Override
    public ResponseEntity<ApiResponse> addVaccination(VaccinationEntity vaccination) {
        try {
            Vaccination newVaccination = vaccinationService.addVaccination(VaccinationMapper.mapToCore(vaccination));
            return httpApiResponse(new HttpApiResponse(VaccinationMapper.mapToEntity(newVaccination)));
        } catch (CMSException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> addVaccinationToPatient(String patientId, PatientVaccination vaccination) {
        try {
            com.ilt.cms.core.entity.patient.PatientVaccination patientVaccination =
                    vaccinationService.addVaccinationToPatient(patientId, PatientMapper.mapToPatientVaccinationCore(vaccination));
            return httpApiResponse(new HttpApiResponse(PatientMapper.mapToPatientVaccinationEntity(patientVaccination)));
        } catch (CMSException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

}
