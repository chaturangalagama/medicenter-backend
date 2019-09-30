package com.ilt.cms.repository.clinic;

import com.ilt.cms.core.entity.label.Label;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelRepository extends MongoRepository<Label, String> {

    @ExistsQuery(value = "{'name' : ?0 }")
    boolean checkNameExists(String name);

    Label findByName(String name);
}
