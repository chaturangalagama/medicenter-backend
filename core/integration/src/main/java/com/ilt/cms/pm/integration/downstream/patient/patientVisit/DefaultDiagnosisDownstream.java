package com.ilt.cms.pm.integration.downstream.patient.patientVisit;

import com.ilt.cms.core.entity.diagnosis.Diagnosis;
import com.ilt.cms.downstream.DiagnosisDownstream;
import com.ilt.cms.pm.business.service.patient.patientVisit.DiagnosisService;
import com.ilt.cms.pm.integration.mapper.patient.patientVisit.DiagnosisMapper;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;
@Service
public class DefaultDiagnosisDownstream implements DiagnosisDownstream {

    private DiagnosisService diagnosisService;

    public DefaultDiagnosisDownstream(DiagnosisService diagnosisService){
        this.diagnosisService = diagnosisService;
    }
    @Override
    public ResponseEntity<ApiResponse> searchById(List<String> diagnosisIds) {
        List<Diagnosis> diagnoses = diagnosisService.searchById(diagnosisIds);
        return httpApiResponse(new HttpApiResponse(diagnoses.stream().map(DiagnosisMapper::mapToEntity).collect(Collectors.toList())));

    }

    @Override
    public ResponseEntity<ApiResponse> search(List<String> planIds, String term) {
        List<Diagnosis> search = diagnosisService.search(planIds, term);
        return httpApiResponse(new HttpApiResponse(search.stream().map(DiagnosisMapper::mapToEntity).collect(Collectors.toList())));
    }
}
