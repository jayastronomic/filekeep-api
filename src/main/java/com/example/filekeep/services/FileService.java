package com.example.filekeep.services;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.filekeep.exceptions.FileDoesNotExistException;
import com.example.filekeep.exceptions.FolderDoesNotExistException;
import com.example.filekeep.models.File;
import com.example.filekeep.models.Folder;
import com.example.filekeep.models.User;
import com.example.filekeep.repositories.FileRepository;
import com.example.filekeep.repositories.FolderRepository;

import io.jsonwebtoken.lang.Arrays;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FileService extends ApplicationService {
    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;
    private final S3Service s3sService;
    
    public String uploadFile(MultipartFile file, UUID folderId) throws FolderDoesNotExistException {
        User user = currentUser();
    
        // Create file key for  S3
        String fileKey = user.getId() + "_" + System.currentTimeMillis() + "_" + Arrays.asList(file.getOriginalFilename().split("/")).getLast();
        
        // Upload file to S3 with file key
        s3sService.uploadFileToAWS(file, fileKey);
    
        // Fetch the folder to save in
        Folder folderToSave = folderRepository.findById(folderId)
                                            .orElseThrow(() -> new FolderDoesNotExistException(folderId));
        ;
        
        // Create and save new File entity
        File newFile = File.builder()
            .fileKey(fileKey)
            .size(file.getSize())
            .mimeType(file.getContentType())
            .fileName(Arrays.asList(file.getOriginalFilename().split("/")).getLast())
            .user(user)
            .folder(folderToSave)
            .build();
        
        fileRepository.save(newFile); // Save file
        return "File uploaded successfully: " + file.getName();
    }

    public byte[] downloadFile(String fileKey){
     return s3sService.downloadFileFromAWS(fileKey);
    }

    public String deleteFile(String fileKey){
        File file = fileRepository.findByFileKey(fileKey)
            .orElseThrow(() -> new FileDoesNotExistException(fileKey)); 
        s3sService.deleteFileFromAWS(file.getFileKey());
        fileRepository.delete(file);
        return "File successfully deleted";
    }


    public void saveFile(MultipartFile file, String fileName, Folder folder) {
        File existingFile = fileRepository.findByFileNameAndFolder(fileName, folder);
        if (existingFile == null) uploadFile(file, folder.getId());
        else System.out.println("File already exists: " + existingFile.getFileName());
    }
}
