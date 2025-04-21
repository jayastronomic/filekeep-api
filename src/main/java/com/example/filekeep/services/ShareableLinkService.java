package com.example.filekeep.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.filekeep.dtos.FileData;
import com.example.filekeep.dtos.NewShareableLinkData;
import com.example.filekeep.dtos.ShareableFileData;
import com.example.filekeep.dtos.ShareableFolderData;
import com.example.filekeep.dtos.ShareableLinkData;
import com.example.filekeep.dtos.UpdateShareableLinkData;
import com.example.filekeep.enums.LinkAccessType;
import com.example.filekeep.exceptions.FileDoesNotExistException;
import com.example.filekeep.exceptions.FolderDoesNotExistException;
import com.example.filekeep.exceptions.ShareableLinkDoesNotExistException;
import com.example.filekeep.models.File;
import com.example.filekeep.models.Folder;
import com.example.filekeep.models.ShareableLink;
import com.example.filekeep.repositories.FileRepository;
import com.example.filekeep.repositories.FolderRepository;
import com.example.filekeep.repositories.ShareableLinkRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShareableLinkService extends ApplicationService {
    private final ShareableLinkRepository shareableLinkRepository;
    private final FolderRepository folderRepository;
    private final FileRepository fileRepository;
    private final S3Service s3Service;

    public ShareableLinkData createShareableLink(NewShareableLinkData data){
        ShareableLink newLink = new ShareableLink();
        UUID id = data.id(); 

        if(data.type().equals("folder")){
            Folder folder = folderRepository.findById(id)
                                            .orElseThrow(() -> new FolderDoesNotExistException(id));
            if(folder.getShareableLink() != null) return new ShareableLinkData(folder.getShareableLink());
            newLink.setFolder(folder);      
        } else {
            File file = fileRepository.findById(id)
                                            .orElseThrow(() -> new FileDoesNotExistException(id));
            if(file.getShareableLink() != null) return new ShareableLinkData(file.getShareableLink());
            newLink.setFile(file); 
        }
        
       ShareableLink savedShareableLink = shareableLinkRepository.save(newLink);
       return new ShareableLinkData(savedShareableLink);
    }


    public ShareableFileData getShareableFile(String token){
        ShareableLink shareableLink = shareableLinkRepository.findByToken(token)
                                        .orElseThrow(() -> new ShareableLinkDoesNotExistException(token));
        byte[] fileContentStream = s3Service.downloadFileFromAWS(shareableLink.getFile().getFileKey());
        return new ShareableFileData(shareableLink.getFile(), fileContentStream);
    }

    public ShareableFolderData getShareableFolder(String token){
        ShareableLink shareableLink = shareableLinkRepository.findByToken(token)
                                        .orElseThrow(() -> new ShareableLinkDoesNotExistException(token));
        return new ShareableFolderData(shareableLink.getFolder());
    }

    public FileData updateFileShareableLinkAccess(String token, UpdateShareableLinkData linkData){
        ShareableLink shareableLink = shareableLinkRepository.findByToken(token)
                                        .orElseThrow(() -> new ShareableLinkDoesNotExistException(token));
        shareableLink.setAccessType(LinkAccessType.valueOf(linkData.linkAccessType()));
        return  new FileData(shareableLinkRepository.save(shareableLink).getFile());
    }
}
