package com.example.filekeep.controllers;

import com.example.filekeep.models.User;
import com.example.filekeep.reponses.ApiResponse;
import com.example.filekeep.enums.Status;
import com.example.filekeep.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody User payload){
        return ResponseEntity
                .created(URI.create("/api/v1/auth/register"))
                .body(ApiResponse.<String>builder()
                        .data(this.authService.register(payload))
                        .message("Account created")
                        .status(Status.SUCCESS)
                        .path("/api/v1/auth/register")
                        .build()
                );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody User payload){
        return ResponseEntity
                .ok()
                .body(ApiResponse.<String>builder()
                        .data(this.authService.login(payload))
                        .message("Successfully logged in")
                        .status(Status.SUCCESS)
                        .path("/api/v1/auth/login")
                        .build()
                );
    }

    @GetMapping("/logged_in")
    public ResponseEntity<ApiResponse<User>> loggedIn() {
        return ResponseEntity
                .ok()
                .body(ApiResponse.<User>builder()
                .data(this.authService.isLoggedIn())
                .message("User is authenticated")
                .status(Status.SUCCESS)
                .path("/api/v1/auth/logged_in")
                .build()
                );
        
    }
    
}
