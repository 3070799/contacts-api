package com.ohol.pavel.contactsapi.outbound.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;

import static com.ohol.pavel.contactsapi.config.ApplicationProfile.DEFAULT;

@AllArgsConstructor
@Component
@Profile(DEFAULT)
public class DefaultOutboundAmazonS3Port implements OutboundAmazonS3Port {

    private final AmazonS3 s3Client;

    @Override
    public PutObjectResult upload(String bucketName, String fileName, File file) {
        return s3Client.putObject(new PutObjectRequest(bucketName, fileName, file));
    }

    @Override
    public S3Object download(String bucketName, String key) {
        return s3Client.getObject(bucketName, key);
    }

    @Override
    public void delete(String bucketName, String key) {
        s3Client.deleteObject(bucketName, key);
    }
}
