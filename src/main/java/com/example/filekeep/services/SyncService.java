package com.example.filekeep.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.filekeep.exceptions.FolderDoesNotExistException;
import com.example.filekeep.models.File;
import com.example.filekeep.models.Folder;
import com.example.filekeep.models.User;
import com.example.filekeep.repositories.FileRepository;
import com.example.filekeep.repositories.FolderRepository;

import io.jsonwebtoken.lang.Arrays;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SyncService extends ApplicationService {
    private final FolderRepository folderRepository;
    private final FileRepository fileRepository;
    private final FileService fileService;
    private final S3Service s3Service;
    
    public String sync(MultipartFile[] files, List<String> directories ) {
        User currentUser = currentUser();
        Folder home = folderRepository.getRootFolder(currentUser.getId())
                .orElseThrow(() -> new FolderDoesNotExistException("home"));
        
        Map<String, Folder> createdFolders = new HashMap<>();
        
        // Step 1: Create empty directories
        if (directories != null) {
            for (String dirPath : directories) {
                List<String> pathParts = Arrays.asList(dirPath.split("/"));
                Folder parentFolder = home;
                String currentPath = "";
        
                for (String part : pathParts) {
                    currentPath = currentPath.isEmpty() ? part : currentPath + "/" + part;
        
                    Folder existingFolder = createdFolders.get(currentPath);
                    if (existingFolder == null) {
                        existingFolder = parentFolder.getFolderByName(part);
                    }
        
                    if (existingFolder == null) {
                        existingFolder = new Folder(part, currentUser, parentFolder);
                        existingFolder = folderRepository.save(existingFolder);
                        createdFolders.put(currentPath, existingFolder);
                    }
        
                    parentFolder = existingFolder;
                }
            }
        }
        
        // Step 2: Handle file uploads
        for (MultipartFile file : files) {
            String relativePath = file.getOriginalFilename();
            List<String> pathParts = Arrays.asList(relativePath.split("/"));
            List<String> folders = pathParts.subList(0, pathParts.size() - 1);
            Folder parentFolder = home;
            String currentPath = "";
        
            for (String folder : folders) {
                currentPath = currentPath.isEmpty() ? folder : currentPath + "/" + folder;
        
                Folder existingFolder = createdFolders.get(currentPath);
                if (existingFolder == null) {
                    existingFolder = parentFolder.getFolderByName(folder);
                }
                if (existingFolder == null) {
                    existingFolder = new Folder(folder, currentUser, parentFolder);
                    existingFolder = folderRepository.save(existingFolder);
                    createdFolders.put(currentPath, existingFolder);
                }
        
                parentFolder = existingFolder;
            }
        
            fileService.saveFile(file, pathParts.get(pathParts.size() - 1), parentFolder);
        }
        return "synced";
        
    }


    public byte[] getZipData(){
        User currentUser = currentUser();
        List<File> userFiles = fileRepository.findFilesByUserId(currentUser.getId());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (File file : userFiles) {
                byte[] content = s3Service.downloadFileFromAWS(file.getFileKey());

                ZipEntry zipEntry = new ZipEntry(file.getFileRemoteFilePath());
                zos.putNextEntry(zipEntry);
                zos.write(content);
                zos.closeEntry();
            }
            zos.finish();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to zip user files", e);
        }
    }
}
