package com.example.filekeep.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record NewUserDto(
    @Email(message = "Invalid email")
    @NotBlank(message = "Email cannot be blank") String email,
    @NotBlank(message = "Password cannot be blank") String password, 
    @NotBlank(message = "Password confirmation cannot be blank") String passwordConfirmation) {
    
}
