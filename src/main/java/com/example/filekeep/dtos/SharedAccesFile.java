package com.example.filekeep.dtos;

import java.util.UUID;

import com.example.filekeep.models.SharedAccess;

import lombok.Getter;

@Getter
public class SharedAccesFile extends SharedAccessData {
    private final UUID id;
    private final String fileName;
    private final long size;
    private final String mimeType;
    private final String fileKey;


    public SharedAccesFile(SharedAccess sharedAccess){
        this.id = sharedAccess.getFile().getId();
        this.fileName = sharedAccess.getFile().getFileName();
        this.size = sharedAccess.getFile().getSize();
        this.mimeType = sharedAccess.getFile().getMimeType();
        this.fileKey = sharedAccess.getFile().getFileKey();
    }
}
