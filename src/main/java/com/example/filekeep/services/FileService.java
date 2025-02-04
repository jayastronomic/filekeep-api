package com.example.filekeep.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.filekeep.models.File;
import com.example.filekeep.models.Folder;
import com.example.filekeep.repositories.FileRepository;
import com.example.filekeep.repositories.FolderRepository;

@Service
public class FileService extends ApplicationService {
    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;
    private final S3Service s3sService;

    public FileService(S3Service s3Service, FileRepository fileRepository, FolderRepository folderRepository) {
        this.s3sService = s3Service;
        this.fileRepository = fileRepository;
        this.folderRepository = folderRepository;
    }

    public String uploadFile(MultipartFile file, String folderName) {
        String fileKey = currentUser().getId() + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        s3sService.uploadFileToAWS(file, fileKey);
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
     return s3sService.downloadFileFromAWS(fileKey);
    }

    public List<File> getUserFiles(){
        return fileRepository.findByUserId(currentUser().getId());  
    }

    public String deleteFile(String fileKey){
        boolean isDeleted = s3sService.deleteFileFromAWS(fileKey);
        if(!isDeleted) return "File did not delete successfully";
        File file = fileRepository.findByFileKey(fileKey)
            .orElseThrow(() -> new RuntimeException("File not found: " + fileKey));
        fileRepository.delete(file);
        return "File successfully deleted";
    }
}
