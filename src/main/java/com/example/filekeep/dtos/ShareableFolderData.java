package com.example.filekeep.dtos;

import com.example.filekeep.models.Folder;

import lombok.Getter;

@Getter
public class ShareableFolderData {
    private final FolderData folderData;

    public ShareableFolderData(Folder folder){
        this.folderData = new FolderData(folder);
    }
}
