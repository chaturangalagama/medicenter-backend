package com.ilt.cms.repository.clinic;

import com.ilt.cms.core.entity.Postcode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostcodeRepository extends MongoRepository<Postcode, String> {


    Postcode findFirstByCode(String code);

}
