package com.ilt.cms.repository.clinic;

import com.ilt.cms.core.entity.RunningNumber;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RunningNumberRepository extends MongoRepository<RunningNumber, String> {


}
