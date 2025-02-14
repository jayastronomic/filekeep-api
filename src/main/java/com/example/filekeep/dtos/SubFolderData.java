package com.example.filekeep.dtos;

import java.util.UUID;

import com.example.filekeep.models.Folder;
import lombok.Getter;

@Getter
public class SubFolderData {
    private UUID id;
    private String folderName;

    public SubFolderData(Folder folder){
        this.id = folder.getId();
        this.folderName = folder.getFolderName();
    }
}
