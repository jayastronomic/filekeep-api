package com.example.filekeep.exceptions;

public class PasswordMismatchException extends RuntimeException{
    public PasswordMismatchException(){
        super("Passwords do not match");
    }
}
