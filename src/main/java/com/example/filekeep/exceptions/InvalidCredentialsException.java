package com.example.filekeep.exceptions;

/**
 * Custom exception class for indicating that a user already exists in the system.
 */
public class InvalidCredentialsException extends RuntimeException {


    public InvalidCredentialsException(){
        super("Invalid email/password");
    }
}
