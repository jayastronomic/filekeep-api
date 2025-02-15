package com.example.filekeep.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.filekeep.dtos.ShareFileData;
import com.example.filekeep.dtos.ShareFolderData;
import com.example.filekeep.dtos.SharedAccessData;
import com.example.filekeep.enums.AccessType;
import com.example.filekeep.models.File;
import com.example.filekeep.models.Folder;
import com.example.filekeep.models.SharedAccess;
import com.example.filekeep.models.User;
import com.example.filekeep.repositories.FileRepository;
import com.example.filekeep.repositories.FolderRepository;
import com.example.filekeep.repositories.SharedAccessRepository;
import com.example.filekeep.repositories.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class SharedAccessService extends ApplicationService {
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;
    private final SharedAccessRepository sharedAccessRepository;

    public String shareFile(ShareFileData payload) {
        File fileToShare = fileRepository.findById(payload.fileId())
            .orElseThrow(() -> new RuntimeException("File does not exist"));
        
        List<User> shareList = userRepository.findByEmailIn(payload.userEmails());
    
        // Create shared access entries
        List<SharedAccess> sharedAccessList = shareList.stream().map(user -> {
            return new SharedAccess(user, fileToShare, AccessType.VIEW);
        }).toList();
    
        // Save all shared access records in one batch
        sharedAccessRepository.saveAll(sharedAccessList);
    
        return "Successfully shared \"" + 
                fileToShare.getFileName() + 
                "\" with " + 
                shareList.size() + " users.";
    }

    public String shareFolder(ShareFolderData payload) {
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

    public List<SharedAccessData> getSharedAssets(){
        User currentUser = userRepository.findById(currentUser().getId())
                                          .orElseThrow(() -> new RuntimeException("User does not exist"));
        return SharedAccessData.transform(currentUser.getSharedAccesses());
    }
    
    private void shareFolderRecursively(Folder folder, List<User> users, AccessType accessType) {
        // Share the folder itself
        users.forEach(user -> sharedAccessRepository.save(new SharedAccess(user, folder, accessType)));
    
        // Share all files in the folder
        folder.getFiles().forEach(file -> {
            users.forEach(user -> sharedAccessRepository.save(new SharedAccess(user, file, accessType)));
        });
    
        // Recursively share all subfolders and their contents
        folder.getSubFolders().forEach(subFolder -> shareFolderRecursively(subFolder, users, accessType));
    }

}
