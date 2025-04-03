package com.example.filekeep.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.filekeep.exceptions.FolderDoesNotExistException;
import com.example.filekeep.models.Folder;
import com.example.filekeep.models.User;
import com.example.filekeep.repositories.FolderRepository;

import io.jsonwebtoken.lang.Arrays;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SyncService extends ApplicationService {
    private final FolderRepository folderRepository;
    private final FileService fileService;
    
    public String sync(MultipartFile[] files) {
    User currentUser = currentUser();
    Folder home = folderRepository.getRootFolder(currentUser.getId())
                .orElseThrow(() -> new FolderDoesNotExistException("home"));

    // Temporary map to store folders created in this sync operation
    Map<String, Folder> createdFolders = new HashMap<>();

    for (MultipartFile file : files) {
        String relativePath = file.getOriginalFilename(); // webkitRelativePath from frontend
        List<String> pathParts = Arrays.asList(relativePath.split("/"));
        List<String> folders = pathParts.subList(0, pathParts.size() - 1);
        Folder parentFolder = home;

        // Traverse and create/check folders
        String currentPath = "";
        for (String folder : folders) {
            currentPath = currentPath.isEmpty() ? folder : currentPath + "/" + folder;

            // Check if folder exists in memory (already processed in this sync)
            Folder existingFolder = createdFolders.get(currentPath);
            
            // If not in memory, check database
            if (existingFolder == null) {
                existingFolder = parentFolder.getFolderByName(folder);
            }
            
            // If still null, create and save it
            if (existingFolder == null) {
                existingFolder = new Folder(folder, currentUser, parentFolder);
                existingFolder = folderRepository.save(existingFolder);
                createdFolders.put(currentPath, existingFolder); // Store in memory
            }

            parentFolder = existingFolder;
        }

        // Handle the file
        fileService.saveFile(file, pathParts.get(pathParts.size() - 1), parentFolder);
    }
    return "synced";
    }
}
