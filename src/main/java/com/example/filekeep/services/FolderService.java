package com.example.filekeep.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.filekeep.dtos.NewFolderDto;
import com.example.filekeep.models.File;
import com.example.filekeep.models.Folder;
import com.example.filekeep.repositories.FolderRepository;

@Service
public class FolderService extends ApplicationService {
    private final FolderRepository folderRepository;
    private final S3Service s3Service;

    public FolderService(FolderRepository folderRepository, S3Service s3Service) {
        this.folderRepository = folderRepository;
        this.s3Service = s3Service;
    }

    public Folder getFolder(String folderName) {
        return folderRepository.getFolderByUserIdAndFolderName(currentUser().getId(), folderName);
    }

    public Folder createFolder(NewFolderDto payload){
        System.out.println(payload);
        Folder parent = folderRepository.getFolderByUserIdAndFolderName(currentUser().getId(), payload.parentName());
        Folder newFolder = Folder.builder()
                                .folderName(payload.folderName())
                                .parentFolder(parent)
                                .user(currentUser())
                                .build();
        return this.folderRepository.save(newFolder);
    }

    public String deleteFolder(UUID folderId){
        Folder folder = folderRepository.findById(folderId)
        .orElseThrow(() -> new RuntimeException("Folder does not exist with id: " + folderId));
        List<File> allFiles = getAllFilesInFolder(folder);
        List<String> fileKeys = allFiles.stream()
                .map(File::getFileKey) 
                .toList();
        
        if (!fileKeys.isEmpty()) s3Service.deleteFilesFromAws(fileKeys);

        folderRepository.deleteById(folderId);
        return folder.getFolderName() + " folder successfully deleted.";
    }

    private List<File> getAllFilesInFolder(Folder folder) {
        // Get files in the current folder
        List<File> filesInCurrentFolder = folder.getFiles();

        // Recursively collect files from subfolders
        folder.getSubFolders().forEach(subFolder -> {
            filesInCurrentFolder.addAll(getAllFilesInFolder(subFolder));
        });

        return filesInCurrentFolder;
    }
}

