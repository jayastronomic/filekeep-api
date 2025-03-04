package com.example.filekeep.services;

import org.springframework.stereotype.Service;

import com.example.filekeep.dtos.NewShareableLinkData;
import com.example.filekeep.dtos.ShareableLinkData;
import com.example.filekeep.models.File;
import com.example.filekeep.models.Folder;
import com.example.filekeep.models.ShareableLink;
import com.example.filekeep.repositories.FileRepository;
import com.example.filekeep.repositories.FolderRepository;
import com.example.filekeep.repositories.ShareableLinkRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ShareableLinkService extends ApplicationService {
    private final ShareableLinkRepository shareableLinkRepository;
    private final FolderRepository folderRepository;
    private final FileRepository fileRepository;

    public ShareableLinkData createShareableLink(NewShareableLinkData data){
        ShareableLink newLink = new ShareableLink();

        if(data.type().equals("folder")){
            Folder folder = folderRepository.findById(data.id())
                                            .orElseThrow(() -> new RuntimeException("Folder does not exist"));
            if(folder.getShareableLink() != null) return new ShareableLinkData(folder.getShareableLink());
            newLink.setFolder(folder);      
        } else {
            File file = fileRepository.findById(data.id())
                                            .orElseThrow(() -> new RuntimeException("File does not exist"));
            if(file.getShareableLink() != null) return new ShareableLinkData(file.getShareableLink());
            newLink.setFile(file); 
        }
        
       ShareableLink savedShareableLink = shareableLinkRepository.save(newLink);
       return new ShareableLinkData(savedShareableLink);
    }
}
