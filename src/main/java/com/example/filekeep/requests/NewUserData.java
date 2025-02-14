package com.example.filekeep.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record NewUserData(
    @Email(message = "Invalid email")
    @NotBlank(message = "Email cannot be blank") String email,
    @NotBlank(message = "First name cannot be blank") String firstName,
    @NotBlank(message = "Last Name cannot be blank") String lastName, 
    @NotBlank(message = "Password cannot be blank") String password, 
    @NotBlank(message = "Password confirmation cannot be blank") String passwordConfirmation) {
    
}
