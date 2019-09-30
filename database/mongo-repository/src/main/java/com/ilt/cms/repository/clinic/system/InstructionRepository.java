package com.ilt.cms.repository.clinic.system;

import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.system.Instruction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstructionRepository extends MongoRepository<Instruction, String> {

    List<Instruction> findAllByStatus(Status status);

    default List<Instruction> findAllActive() {
        return findAllByStatus(Status.ACTIVE);
    }
}
