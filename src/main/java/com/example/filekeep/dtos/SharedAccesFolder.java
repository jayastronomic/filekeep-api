package com.example.filekeep.dtos;


import java.util.List;
import java.util.UUID;

import com.example.filekeep.models.Folder;
import com.example.filekeep.models.SharedAccess;

import lombok.Getter;

@Getter
public class SharedAccesFolder extends SharedAccessData {
    private final UUID id;
    private final String folderName;
    private final List<String> collaborators;
    private final String owner;
    private final String assetType = "folder";
    private final String sharedOn;
    private final long whoCanAccess;

    public SharedAccesFolder(SharedAccess sharedAccess){
        Folder folder = sharedAccess.getFolder();
        this.id = folder.getId();
        this.folderName = folder.getFolderName();
        this.collaborators = folder
                                .getCollaborators()
                                .stream()
                                .map((sa) -> sa.getCollaborator().getEmail()).toList();
        this.owner = sharedAccess.getOwner().getEmail();
        this.sharedOn = formatDate(sharedAccess.getCreatedAt());
        this.whoCanAccess = folder.getCollaborators().size() + 1;
    }
}