package com.lippo.cms.util;

import com.amazonaws.services.s3.AmazonS3;

/*for storing S3 config when start application*/
public class AWSConfig {
    private static AWSConfig awsConfig;

    private String awsAccessKey;

    private String awsSecretKey;

    private String s3BucketName;

    private long maxFileSize;

    private AmazonS3 amazonS3;

    public static AWSConfig getInstance(String awsAccessKey, String awsSecretKey, String s3BucketName, long maxFileSize, AmazonS3 amazonS3){
        if(awsConfig == null){
            awsConfig = new AWSConfig(awsAccessKey, awsSecretKey, s3BucketName, maxFileSize, amazonS3);
        }
        return awsConfig;
    }

    public AWSConfig() {
    }

    public AWSConfig(String awsAccessKey, String awsSecretKey, String s3BucketName, long maxFileSize, AmazonS3 amazonS3) {
        this.awsAccessKey = awsAccessKey;
        this.awsSecretKey = awsSecretKey;
        this.s3BucketName = s3BucketName;
        this.maxFileSize = maxFileSize;
        this.amazonS3 = amazonS3;
    }

    public String getAwsAccessKey() {
        return awsAccessKey;
    }

    public String getAwsSecretKey() {
        return awsSecretKey;
    }

    public String getS3BucketName() {
        return s3BucketName;
    }

    public long getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public AmazonS3 getAmazonS3() {
        return amazonS3;
    }

    @Override
    public String toString() {
        return "AWSConfig{" +
                "awsAccessKey='" + awsAccessKey + '\'' +
                ", awsSecretKey='" + awsSecretKey + '\'' +
                ", s3BucketName='" + s3BucketName + '\'' +
                ", maxFileSize=" + maxFileSize +
                ", amazonS3=" + amazonS3 +
                '}';
    }
}
