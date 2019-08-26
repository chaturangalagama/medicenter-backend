package com.lippo.cms.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.lippo.cms", "com.lippo.commons.web"})
@EntityScan("com.lippo.cms")
@EnableTransactionManagement
@EnableMongoRepositories(value = "com.lippo.cms.repository")
@EnableMongoAuditing
@EnableJpaRepositories(value = "com.lippo.cms.inventory.repository")
@EnableJpaAuditing
public class InventorySystem {
    public static void main(String[] args) {
        SpringApplication.run(InventorySystem.class, args);
    }
}
