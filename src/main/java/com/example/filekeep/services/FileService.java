package com.example.filekeep.services;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.filekeep.models.File;
import com.example.filekeep.repositories.FileRepository;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


@Service
public class FileService extends ApplicationService {
    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    private final S3Client s3Client;
    private final FileRepository fileRepository;

    public FileService(S3Client s3Client, FileRepository fileRepository) {
        this.s3Client = s3Client;
        this.fileRepository = fileRepository;
    }

    public String uploadFile(MultipartFile file) {
        String fileKey = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File newFile = new File();
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
        newFile.setFileKey(fileKey);
        newFile.build(file);
        fileRepository.save(newFile);
        return "File uploaded successfully: " + fileKey;
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

    public List<File> getUserFiles(){
      return fileRepository.findByUserId(currentUser().getId());  
    }
    
}
