package com.example.filekeep.dtos;

import java.util.Base64;
import java.util.UUID;

import com.example.filekeep.enums.LinkAccessType;
import com.example.filekeep.models.File;

import lombok.Getter;

@Getter
public class ShareableFileData {
    private final UUID ownerId;
    private final String fileName;
    private final String mimeType;
    private final long size;
    private final String content;
    private final LinkAccessType linkAccessType;

    public ShareableFileData(File file, byte[] stream){
        this.ownerId = file.getUser().getId();
        this.fileName = file.getFileName();
        this.mimeType = file.getMimeType();
        this.size = file.getSize();
        this.content = Base64.getEncoder().encodeToString(stream);
        this.linkAccessType = file.getShareableLink().getAccessType();
    }
}
