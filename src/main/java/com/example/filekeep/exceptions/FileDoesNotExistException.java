package com.example.filekeep.exceptions;

import java.util.UUID;

public class FileDoesNotExistException extends RuntimeException {
    public FileDoesNotExistException(UUID id){
        super("File does not exist with id: " + id.toString());
    }

    public FileDoesNotExistException(String fileKey){
        super("File does not exist with fileKey: " + fileKey);
    }
}
