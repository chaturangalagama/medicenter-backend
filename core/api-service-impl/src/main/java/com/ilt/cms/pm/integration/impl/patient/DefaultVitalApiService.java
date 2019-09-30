package com.ilt.cms.pm.integration.impl.patient;

import com.ilt.cms.api.entity.vital.VitalEntity;
import com.ilt.cms.core.entity.vital.Vital;
import com.ilt.cms.downstream.patient.VitalApiService;
import com.ilt.cms.pm.business.service.patient.VitalService;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.StatusCode;
import com.lippo.commons.web.api.HttpApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

@Service
public class DefaultVitalApiService implements VitalApiService {

    private VitalService vitalService;

    public DefaultVitalApiService(VitalService vitalService) {
        this.vitalService = vitalService;
    }

    @Override
    public ResponseEntity listAll(String patientId, LocalDateTime before, LocalDateTime after) {
        Map<String, List<Vital>> vitals = vitalService.listVitals(patientId, after, before);
        return httpApiResponse(new HttpApiResponse(vitals));
    }

    @Override
    public ResponseEntity modify(String vitalId, VitalEntity vital) throws CMSException {
        Vital modify = vitalService.modify(vitalId, createVitalCore(vital));
        return httpApiResponse(new HttpApiResponse(modify));
    }

    @Override
    public ResponseEntity addNew(VitalEntity vital) throws CMSException {
        Vital addVital = vitalService.addVital(createVitalCore(vital));
        return httpApiResponse(new HttpApiResponse(addVital));
    }

    private Vital createVitalCore(VitalEntity vital) {
        return new Vital(vital.getPatientId(), LocalDateTime.now(),
                vital.getCode(), vital.getValue(), vital.getComment());
    }

    @Override
    public ResponseEntity remove(String vitalId) throws CMSException {
        vitalService.deleteVital(vitalId);
        return httpApiResponse(new HttpApiResponse(StatusCode.S0000));
    }

    @Override
    public ResponseEntity modify(List<VitalEntity> vitals) throws CMSException {
        List<Vital> list = new ArrayList<>();
        for (VitalEntity vital : vitals) {
            Vital modifiedVital = vitalService.modify(vital.getVitalId(), createVitalCore(vital));
            list.add(modifiedVital);
        }
        return httpApiResponse(new HttpApiResponse(list));
    }

    @Override
    public ResponseEntity addNew(List<VitalEntity> vitalEntities) throws CMSException {
        List<Vital> coreEntity = vitalEntities.stream()
                .map(this::createVitalCore)
                .collect(Collectors.toList());
        List<Vital> vitalResponses = vitalService.addVital(coreEntity);
        return httpApiResponse(new HttpApiResponse(vitalResponses));
    }
}
