package com.example.filekeep.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.filekeep.exceptions.FolderDoesNotExistException;
import com.example.filekeep.models.File;
import com.example.filekeep.models.Folder;
import com.example.filekeep.models.User;
import com.example.filekeep.repositories.FileRepository;
import com.example.filekeep.repositories.FolderRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FileService extends ApplicationService {
    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;
    private final S3Service s3sService;
    
    public String uploadFile(MultipartFile file, String folderName) throws FolderDoesNotExistException {
        User user = currentUser();
    
        // Create file key for  S3
        String fileKey = user.getId() + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        
        // Upload file to S3 with file key
        s3sService.uploadFileToAWS(file, fileKey);
    
        // Fetch the folder to save in
        Folder folder = folderRepository.getFolderByUserIdAndFolderName(user.getId(), folderName)
                                            .orElseThrow(() -> new FolderDoesNotExistException(folderName));
        ;
        
        // Create and save new File entity
        File newFile = File.builder()
            .fileKey(fileKey)
            .size(file.getSize())
            .mimeType(file.getContentType())
            .fileName(file.getOriginalFilename())
            .user(user)
            .folder(folder)
            .build();
        
        fileRepository.save(newFile); // Save file
    
        return "File uploaded successfully: " + file.getName();
    }

    public byte[] downloadFile(String fileKey){
     return s3sService.downloadFileFromAWS(fileKey);
    }

    public String deleteFile(String fileKey){
        s3sService.deleteFileFromAWS(fileKey);
        File file = fileRepository.findByFileKey(fileKey)
            .orElseThrow(() -> new RuntimeException("File not found: " + fileKey));
        fileRepository.delete(file);
        return "File successfully deleted";
    }
}
