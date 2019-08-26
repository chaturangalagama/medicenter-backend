package com.ilt.cms.repository.spring.coverage;

import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.coverage.CoveragePlan;
import com.ilt.cms.core.entity.coverage.MedicalCoverage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface MedicalCoverageRepository extends MongoRepository<MedicalCoverage, String> {

    @Query(value = "{'$or': [{'_id' : ?0}, {'name' : {$regex : ?1, $options: 'i'}}, {'code' : {$regex : ?1, $options: 'i'}}]}")
    List<MedicalCoverage> findByIdOrNameLikeOrCodeLike(String id, String name, Pageable pageable);

    @ExistsQuery(value = "{'code' : ?0 }")
    boolean findIfMedicalCoverageCodeExists(String code);

    @ExistsQuery(value = "{'coveragePlans.code' : ?0 }")
    boolean findIfMedicalPlanCodeExists(String code);

    List<MedicalCoverage> findByStatus(Status status);

    MedicalCoverage findByIdAndStatus(String medicalCoverageId, Status status);

    @Query(value = "{'$and': [{'coveragePlans.id': ?0}, {'status': ?1}]}")
    List<MedicalCoverage> findMedicalCoverageByPlanId(String coveragePlanId, Status status);

    @Query(value = "{'$and': [{'coveragePlans.id': ?0}]}")
    MedicalCoverage findMedicalCoverageByPlanId(String coveragePlanId);

    List<MedicalCoverage> findByType(MedicalCoverage.CoverageType coverageType);

    MedicalCoverage findMedicalCoverageByNameIs(String name);

    List<MedicalCoverage> findMedicalCoveragesByNameIn(Collection<String> name);

}