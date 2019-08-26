package com.ilt.cms.repository.spring;

import com.ilt.cms.core.entity.consultation.ImmunisationGiven;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImmunisationGivenRepository extends MongoRepository<ImmunisationGiven, String> {

}
