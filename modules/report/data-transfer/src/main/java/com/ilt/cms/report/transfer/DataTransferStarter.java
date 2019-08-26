package com.ilt.cms.report.transfer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.ilt.cms", "com.lippo.commons.web"})
@EntityScan("com.ilt.cms")
@EnableTransactionManagement
@EnableMongoRepositories(value = "com.ilt.cms")
@EnableMongoAuditing
public class DataTransferStarter {
    public static void main(String[] args) {
        SpringApplication.run(DataTransferStarter.class, args);
    }

}
