package com.example.filekeep.dtos;

import java.util.UUID;

import com.example.filekeep.models.Folder;
import lombok.Getter;

@Getter
public class SubFolderData {
    private final UUID id;
    private final String folderName;
    private final long whoCanAccess;

    public SubFolderData(Folder folder){
        this.id = folder.getId();
        this.folderName = folder.getFolderName();
        this.whoCanAccess = folder.getCollaborators().size() + 1;
    }
}
