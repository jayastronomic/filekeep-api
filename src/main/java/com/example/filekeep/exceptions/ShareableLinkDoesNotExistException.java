package com.example.filekeep.exceptions;

public class ShareableLinkDoesNotExistException extends RuntimeException {
    public ShareableLinkDoesNotExistException(String token){
        super("Shareable Link does not exist with token: " + token);
    }
}
