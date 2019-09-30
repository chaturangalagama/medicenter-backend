package com.ilt.cms.pm.business.service.clinic.system;

import com.ilt.cms.core.entity.Postcode;
import com.ilt.cms.database.clinic.system.PostcodeDatabaseService;
import com.ilt.cms.pm.business.service.clinic.AllergyGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PostcodeService {
    private static final Logger logger = LoggerFactory.getLogger(AllergyGroupService.class);

    private PostcodeDatabaseService postcodeDatabaseService;

    public PostcodeService(PostcodeDatabaseService postcodeDatabaseService) {
        this.postcodeDatabaseService = postcodeDatabaseService;
    }


    public Postcode findPostcode(String code) {
        Postcode postcode = postcodeDatabaseService.findFirstByCode(code);
        logger.debug("finding postal code for [" + postcode + "] found[" + postcode + "]");
        return postcode;
    }
}
