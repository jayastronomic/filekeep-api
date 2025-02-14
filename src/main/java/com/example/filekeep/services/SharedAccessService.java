package com.example.filekeep.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.filekeep.enums.AccessType;
import com.example.filekeep.models.File;
import com.example.filekeep.models.Folder;
import com.example.filekeep.models.SharedAccess;
import com.example.filekeep.models.User;
import com.example.filekeep.repositories.FileRepository;
import com.example.filekeep.repositories.FolderRepository;
import com.example.filekeep.repositories.SharedAccessRepository;
import com.example.filekeep.repositories.UserRepository;
import com.example.filekeep.requests.ShareFileRequest;
import com.example.filekeep.requests.ShareFolderRequest;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class SharedAccessService extends ApplicationService {
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;
    private final SharedAccessRepository sharedAccessRepository;

    public String shareFile(ShareFileRequest payload) {
        File fileToShare = fileRepository.findById(payload.fileId())
            .orElseThrow(() -> new RuntimeException("File does not exist"));
        
        List<User> shareList = userRepository.findByEmailIn(payload.userEmails());
    
        // Create shared access entries
        List<SharedAccess> sharedAccessList = shareList.stream().map(user -> {
            SharedAccess sharedAccess = new SharedAccess();
            sharedAccess.setFile(fileToShare);
            sharedAccess.setUser(user);
            sharedAccess.setAccessType(AccessType.VIEW);
            return sharedAccess;
        }).toList();
    
        // Save all shared access records in one batch
        sharedAccessRepository.saveAll(sharedAccessList);
    
        return "Successfully shared \"" + 
                fileToShare.getFileName() + 
                "\" with " + 
                shareList.size() + " users.";
    }

    public String shareFolder(ShareFolderRequest payload) {
        Folder folderToShare = folderRepository.findById(payload.folderId())
            .orElseThrow(() -> new RuntimeException("Folder does not exist"));
    
        List<User> shareList = userRepository.findByEmailIn(payload.userEmails());
    
        // Recursively share the folder and its contents
        shareFolderRecursively(folderToShare, shareList, AccessType.VIEW);
    
        return "Successfully shared \"" + 
                folderToShare.getFolderName() + 
                "\" and it's contents with " + 
                shareList.size() + " users.";
    }
    
    private void shareFolderRecursively(Folder folder, List<User> users, AccessType accessType) {
        // Share the folder itself
        users.forEach(user -> {
            SharedAccess sharedAccess = new SharedAccess();
            sharedAccess.setFolder(folder);
            sharedAccess.setUser(user);
            sharedAccess.setAccessType(accessType);
            sharedAccessRepository.save(sharedAccess);
        });
    
        // Share all files in the folder
        folder.getFiles().forEach(file -> {
            users.forEach(user -> {
                SharedAccess fileAccess = new SharedAccess();
                fileAccess.setFile(file);
                fileAccess.setUser(user);
                fileAccess.setAccessType(accessType);
                sharedAccessRepository.save(fileAccess);
            });
        });
    
        // Recursively share all subfolders and their contents
        folder.getSubFolders().forEach(subFolder -> shareFolderRecursively(subFolder, users, accessType));
    }

}
