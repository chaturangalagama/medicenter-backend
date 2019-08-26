package com.ilt.cms.inventory;


import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;

@TestConfiguration
public class SpringTestRepositoryConfiguration {

    @MockBean
    public MongoTemplate mongoTemplate;
}
