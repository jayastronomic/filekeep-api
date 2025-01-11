package com.example.filekeep.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.filekeep.config.AuthUserDetails;
import com.example.filekeep.models.User;

@Service
public class ApplicationService {
      /**
     * Retrieves the current user from the security context.
     *
     * @return The User object representing the currently authenticated user.
     */
    public static User currentUser(){
        AuthUserDetails authUserDetails = (AuthUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return authUserDetails.getUser();
    }
}
