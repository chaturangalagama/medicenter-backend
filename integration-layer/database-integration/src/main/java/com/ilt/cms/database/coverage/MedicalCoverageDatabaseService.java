package com.ilt.cms.database.coverage;

import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.coverage.CoveragePlan;
import com.ilt.cms.core.entity.coverage.MedicalCoverage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MedicalCoverageDatabaseService {
    List<MedicalCoverage> findByIdOrNameLikeOrCodeLike(String id, String name, Pageable pageable);

    boolean findIfMedicalCoverageCodeExists(String code);

    boolean findIfMedicalPlanCodeExists(String code);

    List<MedicalCoverage> findByStatus(Status status);

    MedicalCoverage findOne(String medicalCoverageId);

    boolean doesPlanExists(String medicalCoverageId, String planId);

    boolean doesCoverageNameExists(String coverageName);

    CoveragePlan findPlan(String medicalCoverageId, String planId);

    MedicalCoverage findPlanByMedicalCoverageId(String medicalCoverageId);

    CoveragePlan findPlan(String planId);

    CoveragePlan findActivePlan(String planId);

    //MedicalCoverageDatabaseService getMedicalCoverageRepository();

    MedicalCoverage save(MedicalCoverage coverage);

    boolean delete(String medicalCoverageId);

    Page<MedicalCoverage> findAll(PageRequest name);

    List<MedicalCoverage> findAll();

    List<MedicalCoverage> findMedicalCoveragesByPlanId(String planId, Status status);

}
