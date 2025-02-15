package com.example.filekeep.exceptions;

public class FolderDoesNotExistException extends RuntimeException{
    public FolderDoesNotExistException(String folderName){
        super("Folder does not exist with name: " + folderName);
    }
}
