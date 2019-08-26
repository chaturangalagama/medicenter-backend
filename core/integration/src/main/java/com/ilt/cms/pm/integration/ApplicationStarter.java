package com.ilt.cms.pm.integration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.lippo.cms.util.AWSConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.ilt.cms", "com.lippo.commons.web"})
@EntityScan("com.ilt.cms")
@EnableTransactionManagement
@EnableMongoRepositories(value = "com.ilt.cms.repository")
@EnableMongoAuditing
@EnableScheduling
public class ApplicationStarter {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationStarter.class, args);
    }

    @Value("${aws.access.key}")
    private String awsAccessKey;
    @Value("${aws.secret.key}")
    private String awsSecretKey;
    @Value("${s3.backet.name}")
    private String s3BucketName;
    @Value("${max.file.size}")
    private long maxFileSize;

    @Bean
    public AWSConfig authenticateAws() {
        return AWSConfig.getInstance(awsAccessKey, awsSecretKey, s3BucketName, maxFileSize,
                AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKey, awsSecretKey)))
                .withRegion(Regions.AP_SOUTHEAST_1)
                .build());

    }

}
