package com.ohol.pavel.contactsapi.outbound.aws;

import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

import java.io.File;

public interface OutboundAmazonS3Port {

    PutObjectResult upload(String bucketName, String fileName, File file);

    S3Object download(String bucketName, String key);

    void delete(String bucketName, String key);
}
