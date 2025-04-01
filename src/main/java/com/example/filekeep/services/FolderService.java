package com.example.filekeep.services;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.filekeep.dtos.FolderData;
import com.example.filekeep.dtos.NewFolderData;
import com.example.filekeep.exceptions.FolderDoesNotExistException;
import com.example.filekeep.models.File;
import com.example.filekeep.models.Folder;
import com.example.filekeep.models.User;
import com.example.filekeep.repositories.FolderRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class FolderService extends ApplicationService {
    private final FolderRepository folderRepository;
    private final FileService fileService;
    private final S3Service s3Service;
    private static final Logger logger = LoggerFactory.getLogger(FolderService.class);

    public FolderData getFolder(UUID folderId) throws FolderDoesNotExistException {
        Folder folder = folderRepository.findById(folderId)
                            .orElseThrow(() -> new FolderDoesNotExistException(folderId.toString()));
        return new FolderData(folder);
    }

    public FolderData createFolder(NewFolderData payload) throws FolderDoesNotExistException {
        User currentUser = currentUser();
        Folder parentFolder = folderRepository.findById(payload.parentFolderId())
                                .orElseThrow(() -> new FolderDoesNotExistException(payload.parentFolderId().toString()));
        Folder newFolder = new Folder(payload.folderName(), currentUser, parentFolder);
        Folder savedFolder = this.folderRepository.save(newFolder);
        return new FolderData(savedFolder);
    }

    public String deleteFolder(UUID folderId) throws RuntimeException {
        Folder folder = folderRepository.findById(folderId)
        .orElseThrow(() -> new RuntimeException("Folder does not exist with id: " + folderId));
        List<File> allFiles = getAllFilesInFolder(folder);
        List<String> fileKeys = allFiles.stream().map(File::getFileKey).toList();
        
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

    public String sync(MultipartFile[] files) {
        User currentUser = currentUser();
        Folder home = folderRepository.getRootFolder(currentUser.getId())
                    .orElseThrow(() -> new FolderDoesNotExistException("home"));

        for (MultipartFile file : files) {
            String relativePath = file.getOriginalFilename(); // webkitRelativePath from frontend
            List<String> pathParts = Arrays.asList(relativePath.split("/"));
            List<String> folders = pathParts.subList(1, pathParts.size() - 1);
            Folder parentFolder = home;

            // Skip .DS_Store files
            if (relativePath.equals(".DS_Store") || relativePath.endsWith("/.DS_Store")) {
                logger.info("Skipping .DS_Store file: {}", relativePath);
                continue; // Skip processing this file
            }

            // Traverse and create/check folders
            for (String folder : folders) {
                Folder existingFolder = parentFolder.getFolderByName(folder);                          
                if (existingFolder == null) {
                    existingFolder = new Folder(folder, currentUser, parentFolder);
                    existingFolder = folderRepository.save(existingFolder);
                }
                parentFolder = existingFolder;
            }

            // Handle the file
            fileService.saveFile(file, pathParts.get(pathParts.size() - 1), parentFolder);
        }
        logger.info("\u001B[32mSynced\u001B[0m");
        return "synced";
    }

    public FolderData getHomeFolder() {
      return new FolderData(folderRepository.getRootFolder(currentUser().getId())
                    .orElseThrow(() -> new RuntimeException("Root folder does not exist")));
    }
}

