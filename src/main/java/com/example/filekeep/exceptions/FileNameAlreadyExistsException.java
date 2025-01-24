package com.example.filekeep.exceptions;

public class FileNameAlreadyExistsException extends RuntimeException {
    public FileNameAlreadyExistsException(String fileName){
        super("File alreadys exists with name: " + fileName);
    }
}
