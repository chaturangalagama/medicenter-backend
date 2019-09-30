package com.ilt.cms.db.service.patient.patientVisit;

import com.ilt.cms.core.entity.visit.PatientVisitRegistry;
import com.ilt.cms.database.patient.patientVisit.PatientVisitDatabaseService;
import com.ilt.cms.repository.patient.patientVisit.PatientVisitRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MongoPatientVisitDatabaseService implements PatientVisitDatabaseService {

    private PatientVisitRepository registryRepository;
    private MongoTemplate mongoTemplate;

    public MongoPatientVisitDatabaseService(PatientVisitRepository registryRepository, MongoTemplate mongoTemplate) {
        this.registryRepository = registryRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public PatientVisitRegistry save(PatientVisitRegistry visitRegistry) {
        return registryRepository.save(visitRegistry);
    }

    @Override
    public PatientVisitRegistry searchById(String id) {
        return registryRepository.findById(id).orElse(null);
    }

    @Override
    public List<PatientVisitRegistry> searchByIds(List<String> ids) {
        List<PatientVisitRegistry> visits = new ArrayList<>();
        registryRepository.findAllById(ids).forEach(visits::add);
        return visits;
    }

    @Override
    public PatientVisitRegistry searchByVisitNumber(String visitNumber) {
        return registryRepository.findByVisitNumber(visitNumber).get();
    }

    @Override
    public List<PatientVisitRegistry> listPatientVisits(String patientId) {
        return registryRepository.findAllByPatientId(patientId);
    }

    @Override
    public List<PatientVisitRegistry> listByClinicIdAndStartTime(String clinicId, LocalDateTime start, LocalDateTime end) {
        return registryRepository.findAllByClinicIdAndStartTimeBetween(clinicId, start, end, new Sort(Sort.Direction.ASC, "startTime"));
    }

    @Override
    public void remove(String visitId) {
        registryRepository.deleteById(visitId);
    }

    @Override
    public boolean exists(String visitId) {
        return registryRepository.existsById(visitId);
    }

    @Override
    public Page<PatientVisitRegistry> listPatientVisits(String patientId, int page, int size, Sort sort) {
        Pageable pageable = PageRequest.of(page, size);
        Query query = new Query(Criteria.where("patientId").is(patientId)).with(pageable);
        List<PatientVisitRegistry> list = mongoTemplate.find(query, PatientVisitRegistry.class);
        return PageableExecutionUtils.getPage(list, pageable, () -> mongoTemplate.count(query, PatientVisitRegistry.class));
    }

    @Override
    public Page<PatientVisitRegistry> findByPatientIdAndStartTime(String patientId, LocalDateTime start, LocalDateTime end, int page, int size, Sort sort) {
        return registryRepository.findAllByPatientIdAndStartTimeBetween(patientId, start, end, PageRequest.of(page, size, sort));
    }

    @Override
    public List<PatientVisitRegistry> findByPatientIdAndStartTime(String patientId, LocalDateTime start, LocalDateTime end) {
        return registryRepository.findAllByPatientIdAndStartTimeBetween(patientId, start, end);
    }

    @Override
    public List<PatientVisitRegistry> findByPreferredDoctorIdAndStartTime(String preferredDoctorId, LocalDateTime start, LocalDateTime end) {
        return registryRepository.findALLByPreferredDoctorIdAndStartTimeBetween(preferredDoctorId, start, end);
    }

    @Override
    public List<PatientVisitRegistry> findByPreferredDoctorIdAndClinicIdAndStartTime(String preferredDoctorId, String clinicId, LocalDateTime start, LocalDateTime end) {
        return registryRepository.findALLByPreferredDoctorIdAndClinicIdAndStartTimeBetween(preferredDoctorId, clinicId, start, end);
    }

    @Override
    public List<PatientVisitRegistry> findByClinicIdsAndStartEndTimeBetween(List<String> clinicIds, LocalDateTime start, LocalDateTime end) {
        return registryRepository.findAllByClinicIdInAndStartTimeGreaterThanAndEndTimeLessThan(clinicIds, start, end);
    }

    @Override
    public List<PatientVisitRegistry> searchClinicAndDoctorsByStartTime(List<String> clinicIds, List<String> doctorIds,
                                                                        LocalDateTime startTime, LocalDateTime endTime) {
        Query query = new Query();

        if (clinicIds != null && !clinicIds.isEmpty()) {
            query.addCriteria(Criteria.where("clinicId").in(clinicIds));
        }
        if (doctorIds != null && !doctorIds.isEmpty()) {
            query.addCriteria(Criteria.where("preferredDoctorId").in(doctorIds));
        }
        query.addCriteria(Criteria.where("startTime").gte(startTime).lte(endTime));
        return mongoTemplate.find(query, PatientVisitRegistry.class);
    }
}
