package com.ilt.cms.repository.spring.system;

import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.system.Instruction;
import com.ilt.cms.core.entity.system.PrescriptionDose;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionDoseRepository extends MongoRepository<PrescriptionDose, String> {

    List<PrescriptionDose> findAllByStatus(Status status);

    default List<PrescriptionDose> findAllActive() {
        return findAllByStatus(Status.ACTIVE);
    }

}
