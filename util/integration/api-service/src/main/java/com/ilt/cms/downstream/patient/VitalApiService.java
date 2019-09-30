package com.ilt.cms.downstream.patient;

import com.ilt.cms.api.entity.vital.VitalEntity;
import com.lippo.cms.exception.CMSException;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface VitalApiService {

    ResponseEntity listAll(String patientId, LocalDateTime before, LocalDateTime after);

    ResponseEntity modify(String vitalId, VitalEntity vital) throws CMSException;

    ResponseEntity addNew(VitalEntity vital) throws CMSException;

    ResponseEntity remove(String vitalId) throws CMSException;

    ResponseEntity modify(List<VitalEntity> vitals) throws CMSException;

    ResponseEntity addNew(List<VitalEntity> vitalEntitys) throws CMSException;
}
