package com.example.filekeep.dtos;

import java.util.List;
import java.util.UUID;

import com.example.filekeep.models.File;
import com.example.filekeep.models.SharedAccess;

import lombok.Getter;

@Getter
public class SharedAccesFile extends SharedAccessData {
    private final UUID id;
    private final String fileName;
    private final long size;
    private final String mimeType;
    private final String fileKey;
    private final List<String> collaborators;
    private final String owner;
    private final String assetType = "file";
    private final String sharedOn;
    private final long whoCanAccess;

    public SharedAccesFile(SharedAccess sharedAccess){
        File file = sharedAccess.getFile();
        this.id = file.getId();
        this.fileName = file.getFileName();
        this.size = file.getSize();
        this.mimeType = file.getMimeType();
        this.fileKey = file.getFileKey();
        this.collaborators = file
                                .getCollaborators()
                                .stream()
                                .map((sa) -> sa.getCollaborator().getEmail()).toList();
        this.owner = sharedAccess.getOwner().getEmail();
        this.sharedOn = formatDate(sharedAccess.getCreatedAt());
        this.whoCanAccess = file.getCollaborators().size() + 1;
    }
}
