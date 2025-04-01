package com.example.filekeep.exceptions;

public class FolderDoesNotExistException extends RuntimeException{
    public FolderDoesNotExistException(String id){
        super("Folder does not exist with id: " + id);
    }
}
