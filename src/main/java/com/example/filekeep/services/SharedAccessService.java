package com.example.filekeep.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.filekeep.dtos.ShareData;
import com.example.filekeep.dtos.SharedAccessData;
import com.example.filekeep.enums.AccessType;
import com.example.filekeep.exceptions.FileDoesNotExistException;
import com.example.filekeep.exceptions.FolderDoesNotExistException;
import com.example.filekeep.models.File;
import com.example.filekeep.models.Folder;
import com.example.filekeep.models.SharedAccess;
import com.example.filekeep.models.User;
import com.example.filekeep.repositories.FileRepository;
import com.example.filekeep.repositories.FolderRepository;
import com.example.filekeep.repositories.SharedAccessRepository;
import com.example.filekeep.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SharedAccessService extends ApplicationService {
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;
    private final SharedAccessRepository sharedAccessRepository;

    public String shareFile(ShareData payload) {
        UUID id = payload.id();
        File fileToShare = fileRepository.findById(id)
            .orElseThrow(() -> new FileDoesNotExistException(id));
        
        List<User> shareList = userRepository.findByEmailIn(payload.userEmails());
    
        // Create shared access entries
        List<SharedAccess> sharedAccessList = shareList.stream().map(user -> {
            return new SharedAccess(currentUser(), user, fileToShare, AccessType.VIEW);
        }).toList();
    
        // Save all shared access records in one batch
        sharedAccessRepository.saveAll(sharedAccessList);
    
        return "Successfully shared \"" + 
                fileToShare.getFileName() + 
                "\" with " + 
                shareList.size() + " users.";
    }

    public String shareFolder(ShareData payload) {
        UUID id = payload.id();
        Folder folderToShare = folderRepository.findById(id)
            .orElseThrow(() -> new FolderDoesNotExistException(id));
    
        List<User> shareList = userRepository.findByEmailIn(payload.userEmails());
    
        // Recursively share the folder and its contents
        shareFolderRecursively(folderToShare, shareList, AccessType.VIEW);
    
        return "Successfully shared \"" + 
                folderToShare.getFolderName() + 
                "\" and it's contents with " + 
                shareList.size() + " users.";
    }

    public List<SharedAccessData> getSharedAssets(){
        List<SharedAccess> sharedAssets = sharedAccessRepository.findAllByOwnerIdOrCollaboratorId(currentUser().getId(), currentUser().getId());
        return SharedAccessData.transform(sharedAssets);
    }
    
    private void shareFolderRecursively(Folder folder, List<User> users, AccessType accessType) {
        User currentUser = currentUser();
        // Share the folder itself
        users.forEach(user -> sharedAccessRepository.save(new SharedAccess(currentUser, user, folder, accessType)));
    
        // Share all files in the folder
        folder.getFiles().forEach(file -> {
            users.forEach(user -> sharedAccessRepository.save(new SharedAccess(currentUser, user, file, accessType)));
        });
    
        // Recursively share all subfolders and their contents
        folder.getSubFolders().forEach(subFolder -> shareFolderRecursively(subFolder, users, accessType));
    }

}
