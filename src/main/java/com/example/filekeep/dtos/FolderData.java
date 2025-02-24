package com.example.filekeep.dtos;

import java.util.List;
import java.util.UUID;

import com.example.filekeep.models.Folder;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FolderData {
    private final UUID id;
    private final String folderName;
    private final List<SubFolderData> subFolders;
    private final List<FileData> files;
    private final long whoCanAccess;

    public FolderData(Folder folder){
        this.id = folder.getId();
        this.folderName = folder.getFolderName();
        this.files = folder.getFiles()
                        .stream()
                        .map(file -> new FileData(file))
                        .toList();
        this.subFolders = folder.getSubFolders()
                            .stream()
                            .map(subFolder -> new SubFolderData(subFolder))
                            .toList();
        this.whoCanAccess = folder.getCollaborators().size() + 1;
    }
}
