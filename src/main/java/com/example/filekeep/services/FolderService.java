package com.example.filekeep.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

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
    private final S3Service s3Service;

    public FolderData getFolder(String folderName) throws FolderDoesNotExistException {
        Folder folder = folderRepository.getFolderByUserIdAndFolderName(currentUser().getId(), folderName)
                            .orElseThrow(() -> new FolderDoesNotExistException(folderName));
        return new FolderData(folder);
    }

    public FolderData createFolder(NewFolderData payload) throws FolderDoesNotExistException {
        User currentUser = currentUser();
        Folder parentFolder = folderRepository.getFolderByUserIdAndFolderName(currentUser.getId(), payload.parentName())
                                .orElseThrow(() -> new FolderDoesNotExistException(payload.parentName()));
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
}

