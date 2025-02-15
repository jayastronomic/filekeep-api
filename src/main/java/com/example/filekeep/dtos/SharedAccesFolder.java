package com.example.filekeep.dtos;

import java.util.UUID;

import com.example.filekeep.models.SharedAccess;

import lombok.Getter;

@Getter
public class SharedAccesFolder extends SharedAccessData {
    private final UUID id;
    private final String folderName;

    public SharedAccesFolder(SharedAccess sharedAccess){
        this.id = sharedAccess.getFolder().getId();
        this.folderName = sharedAccess.getFolder().getFolderName();
    }
}
