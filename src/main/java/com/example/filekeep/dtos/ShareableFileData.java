package com.example.filekeep.dtos;

import com.example.filekeep.models.File;

import lombok.Getter;

@Getter
public class ShareableFileData {
    private final FileData file;
    private final byte[] stream;

    public ShareableFileData(File file, byte[] stream){
        this.file = new FileData(file);
        this.stream = stream;
    }
}
