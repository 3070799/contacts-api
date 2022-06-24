package com.ohol.pavel.contactsapi.outbound.aws;

import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;

import static com.ohol.pavel.contactsapi.config.ApplicationProfile.TEST;
import static java.nio.charset.Charset.defaultCharset;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@AllArgsConstructor
@Component
@Profile(TEST)
public class StubbedOutboundAmazonS3Port implements OutboundAmazonS3Port {

    private final static Logger LOGGER = LoggerFactory.getLogger(StubbedOutboundAmazonS3Port.class);

    @Override
    public PutObjectResult upload(String bucketName, String fileName, File file) {
        return new PutObjectResult();
    }

    @Override
    public S3Object download(String bucketName, String key) {
        S3Object s3Object = new S3Object();
        s3Object.setObjectContent(IOUtils.toInputStream(randomAlphabetic(20), defaultCharset()));
        s3Object.setKey(key);
        s3Object.setBucketName(bucketName);
        return s3Object;
    }

    @Override
    public void delete(String bucketName, String key) {
        LOGGER.info("{} deleted from bucket: {}", key, bucketName);
    }
}