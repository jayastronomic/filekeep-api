package com.example.filekeep.services;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Delete;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3Service {
    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    private final S3Client s3Client;

    public S3Service(S3Client s3Client){
        this.s3Client = s3Client;
    }

    public byte[] downloadFileFromAWS(String fileKey){
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


    public void uploadFileToAWS(MultipartFile file, String fileKey){
        try (InputStream inputStream = file.getInputStream()) {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileKey)
                            .build(),
                    RequestBody.fromInputStream(inputStream, file.getSize())
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    public void deleteFileFromAWS(String fileKey){
        try {
            // Create a DeleteObjectRequest
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .build();

            // Perform the delete operation
            s3Client.deleteObject(deleteObjectRequest);

            System.out.println("File deleted successfully: " + fileKey);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file from S3 bucket: " + fileKey, e);
        }
    }

     public void deleteFilesFromAws(List<String> fileKeys) {
        if (fileKeys.isEmpty()) return;

        // Convert fileKeys into S3 ObjectIdentifiers
        List<ObjectIdentifier> objectsToDelete = fileKeys.stream()
                .map(key -> ObjectIdentifier.builder().key(key).build())
                .toList();

        // Build delete request
        DeleteObjectsRequest deleteRequest = DeleteObjectsRequest.builder()
                .bucket(bucketName)
                .delete(Delete.builder().objects(objectsToDelete).build())
                .build();

        // Execute delete request
        s3Client.deleteObjects(deleteRequest);
    }
}
