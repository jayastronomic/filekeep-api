package com.example.filekeep.exceptions;

public class MaxFileStorageException extends RuntimeException {
    public MaxFileStorageException(){
        super("File storage limit reached.");
    }
}
