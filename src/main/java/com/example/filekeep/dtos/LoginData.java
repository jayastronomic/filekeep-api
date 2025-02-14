package com.example.filekeep.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginData(  
    @NotBlank(message = "Email cannot be blank") String email,
    @NotBlank(message = "Password cannot be blank") String password) {
}
