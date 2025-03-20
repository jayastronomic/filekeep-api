package com.example.filekeep.dtos;

import java.util.UUID;

import com.example.filekeep.models.File;
import com.example.filekeep.models.ShareableLink;

import lombok.Getter;

@Getter
public class FileData {
    private UUID id;
    private String fileName;
    private long size;
    private String mimeType;
    private String fileKey;
    private final long whoCanAccess;
    private final ShareableLinkData shareableLink;

    public FileData(File file) {
        this.id = file.getId();
        this.fileName = file.getFileName();
        this.size = file.getSize();
        this.mimeType = file.getMimeType();
        this.fileKey = file.getFileKey();
        this.whoCanAccess = file.getCollaborators().size() + 1;
        this.shareableLink = setShareableLinkData(file.getShareableLink());
    }

    private ShareableLinkData setShareableLinkData(ShareableLink shareableLink){
        if(shareableLink == null) return null;
        return new ShareableLinkData(shareableLink);
    }
}
