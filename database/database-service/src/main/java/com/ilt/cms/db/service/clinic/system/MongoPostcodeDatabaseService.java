package com.ilt.cms.db.service.clinic.system;

import com.ilt.cms.core.entity.Postcode;
import com.ilt.cms.database.clinic.system.PostcodeDatabaseService;
import com.ilt.cms.repository.clinic.PostcodeRepository;
import org.springframework.stereotype.Service;

@Service
public class MongoPostcodeDatabaseService implements PostcodeDatabaseService {

    private PostcodeRepository postcodeRepository;

    public MongoPostcodeDatabaseService(PostcodeRepository postcodeRepository){
        this.postcodeRepository = postcodeRepository;

    }
    @Override
    public Postcode findFirstByCode(String code) {
        return postcodeRepository.findFirstByCode(code);
    }
}
