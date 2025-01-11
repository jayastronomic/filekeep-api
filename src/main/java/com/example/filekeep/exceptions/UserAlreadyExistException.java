package com.example.filekeep.exceptions;

/**
 * Custom exception class for indicating that a user already exists in the system.
 */
public class UserAlreadyExistException extends RuntimeException {

    /**
     * Constructor for UserAlreadyExistsException.
     * @param email The email for the user that already exists
     */
    public UserAlreadyExistException(String email){
        super("User already exists with email: " + email);
    }
}
