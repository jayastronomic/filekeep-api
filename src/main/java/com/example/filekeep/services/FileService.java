package com.example.filekeep.services;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.filekeep.models.File;
import com.example.filekeep.models.Folder;
import com.example.filekeep.repositories.FileRepository;
import com.example.filekeep.repositories.FolderRepository;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


@Service
public class FileService extends ApplicationService {
    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    private final S3Client s3Client;
    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;

    public FileService(S3Client s3Client, FileRepository fileRepository, FolderRepository folderRepository) {
        this.s3Client = s3Client;
        this.fileRepository = fileRepository;
        this.folderRepository = folderRepository;
    }

    public String uploadFile(MultipartFile file, String folderName) {
        String fileKey = currentUser().getId() + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        uploadToAWS(file, fileKey);
        File newFile = new File();
        newFile.setFileKey(fileKey);
        newFile.build(file);
        newFile.setUser(currentUser());
        Folder folder = folderRepository.getFolderByUserIdAndFolderName(currentUser().getId(), folderName);
        folder.getFiles().add(newFile);
        newFile.setFolder(folder);
        folderRepository.save(folder);
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

    public String deleteFile(String fileKey){
        boolean isDeleted = deleteFromAWS(fileKey);
        if(!isDeleted){
            return "File did not delete successfully";
        }
        File file = fileRepository.findByFileKey(fileKey)
            .orElseThrow(() -> new RuntimeException("File not found: " + fileKey));
        fileRepository.delete(file);
        return "File successfully deleted";
    }

    // PRIVATE METHODS

    private boolean uploadToAWS(MultipartFile file, String fileKey){
        try (InputStream inputStream = file.getInputStream()) {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileKey)
                            .build(),
                    RequestBody.fromInputStream(inputStream, file.getSize())
            );
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    private boolean deleteFromAWS(String fileKey){
        try {
            // Create a DeleteObjectRequest
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .build();

            // Perform the delete operation
            s3Client.deleteObject(deleteObjectRequest);

            System.out.println("File deleted successfully: " + fileKey);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file from S3 bucket: " + fileKey, e);
        }
    }
}
