package com.example.filekeep.services;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


@Service
public class FileService {
    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    private final S3Client s3Client;

    public FileService(S3Client s3Client) {
        this.s3Client = s3Client;
    }
      public String uploadFile(MultipartFile file) {
        String fileKey = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        try (InputStream inputStream = file.getInputStream()) {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileKey)
                            .build(),
                    RequestBody.fromInputStream(inputStream, file.getSize())
            );
            return "File uploaded successfully: " + fileKey;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    public byte[] downloadFile(String fileKey){
        try (InputStream inputStream = s3Client.getObject(
                GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileKey)
                        .build())
        ) {
            byte[] fileContent = inputStream.readAllBytes();
            return fileContent;
        } catch (Exception e) {
            throw new RuntimeException("File Not found");
        }
    }
    
}
