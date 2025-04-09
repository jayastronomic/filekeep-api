package com.example.filekeep.exceptions;

import java.util.UUID;

public class FolderDoesNotExistException extends RuntimeException{
    public FolderDoesNotExistException(UUID id){
        super("Folder does not exist with id: " + id.toString());
    }

    public FolderDoesNotExistException(String name){
        super(name + " folder does not exist.");
    }
}
