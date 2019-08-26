package com.ilt.cms.db.service;

import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.coverage.CoveragePlan;
import com.ilt.cms.core.entity.coverage.MedicalCoverage;
import com.ilt.cms.database.coverage.MedicalCoverageDatabaseService;
import com.ilt.cms.repository.CustomMedicalCoverageRepository;
import com.ilt.cms.repository.spring.coverage.MedicalCoverageRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MongoMedicalCoverageDatabaseService implements MedicalCoverageDatabaseService {

    private MedicalCoverageRepository medicalCoverageRepository;
    private CustomMedicalCoverageRepository customMedicalCoverageRepository;

    public MongoMedicalCoverageDatabaseService(MedicalCoverageRepository medicalCoverageRepository, CustomMedicalCoverageRepository customMedicalCoverageRepository){
        this.medicalCoverageRepository = medicalCoverageRepository;
        this.customMedicalCoverageRepository = customMedicalCoverageRepository;
    }

    @Override
    public List<MedicalCoverage> findByIdOrNameLikeOrCodeLike(String id, String name, Pageable pageable) {
        return medicalCoverageRepository.findByIdOrNameLikeOrCodeLike(id, name, pageable);
    }

    @Override
    public boolean findIfMedicalCoverageCodeExists(String code) {
        return medicalCoverageRepository.findIfMedicalCoverageCodeExists(code);
    }

    @Override
    public boolean findIfMedicalPlanCodeExists(String code) {
        return medicalCoverageRepository.findIfMedicalPlanCodeExists(code);
    }

    @Override
    public List<MedicalCoverage> findByStatus(Status status) {
        return medicalCoverageRepository.findByStatus(status);
    }

    @Override
    public MedicalCoverage findOne(String medicalCoverageId) {
        return medicalCoverageRepository.findById(medicalCoverageId).orElse(null);
    }

    @Override
    public boolean doesPlanExists(String medicalCoverageId, String planId) {
        return customMedicalCoverageRepository.doesPlanExists(medicalCoverageId, planId);
    }

    @Override
    public MedicalCoverage findPlanByMedicalCoverageId(String medicalCoverageId) {
        return customMedicalCoverageRepository.findPlanByMedicalCoverageId(medicalCoverageId);
    }

    @Override
    public boolean doesCoverageNameExists(String coverageName){
        return customMedicalCoverageRepository.doesCoverageNameExists(coverageName);
    }

    @Override
    public CoveragePlan findPlan(String medicalCoverageId, String planId) {
        return customMedicalCoverageRepository.findPlan(medicalCoverageId, planId);
    }

    @Override
    public CoveragePlan findPlan(String planId) {
        return customMedicalCoverageRepository.findPlan(planId);
    }

    @Override
    public CoveragePlan findActivePlan(String planId) {
        return customMedicalCoverageRepository.findActivePlan(planId);
    }

    @Override
    public MedicalCoverage save(MedicalCoverage coverage) {
        return medicalCoverageRepository.save(coverage);
    }

    @Override
    public boolean delete(String medicalCoverageId) {
        medicalCoverageRepository.deleteById(medicalCoverageId);
        return true;
    }

    @Override
    public Page<MedicalCoverage> findAll(PageRequest name) {
        return medicalCoverageRepository.findAll(name);
    }

    @Override
    public List<MedicalCoverage> findAll() {
        return medicalCoverageRepository.findAll();
    }

    @Override
    public List<MedicalCoverage> findMedicalCoveragesByPlanId(String planId, Status status) {
        return medicalCoverageRepository.findMedicalCoverageByPlanId(planId, status);
    }


}
