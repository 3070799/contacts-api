package com.ohol.pavel.contactsapi.aws.s3;

import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.ohol.pavel.contactsapi.outbound.aws.OutboundAmazonS3Port;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * @author Pavel Ohol
 */
@Service
@RequiredArgsConstructor
public class AmazonS3Service {

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    private final OutboundAmazonS3Port outboundAmazonS3Port;

    public String uploadFile(MultipartFile file, String imgUuid) {
        String fileName = System.currentTimeMillis() + "-" + imgUuid + "-" + file.getOriginalFilename();
        File fileObj = convertMultiPartFileToFile(file);
        outboundAmazonS3Port.upload(bucketName, fileName, fileObj);
        fileObj.delete();
        return fileName;
    }

    public byte[] downloadFile(String fileName) {
        S3Object s3Object = outboundAmazonS3Port.download(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteFile(String fileName) {
        outboundAmazonS3Port.delete(bucketName, fileName);
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convertedFile;
    }

}