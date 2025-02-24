package com.example.filekeep.controllers;

import com.example.filekeep.reponses.ApiSuccessResponse;
import com.example.filekeep.dtos.LoginData;
import com.example.filekeep.dtos.NewUserData;
import com.example.filekeep.dtos.UserData;
import com.example.filekeep.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiSuccessResponse<String>> register(@Valid @RequestBody NewUserData payload){
        return ResponseEntity
                .created(URI.create("/api/v1/auth/register"))
                .body(ApiSuccessResponse.<String>builder()
                        .data(authService.register(payload))
                        .message("Account created")
                        .path("/api/v1/auth/register")
                        .build()
                );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiSuccessResponse<String>> login(@Valid @RequestBody LoginData payload){
        return ResponseEntity
                .ok()
                .body(ApiSuccessResponse.<String>builder()
                        .data(authService.login(payload))
                        .message("Successfully logged in")
                        .path("/api/v1/auth/login")
                        .build()
                );
    }

    @GetMapping("/logged_in")
    public ResponseEntity<ApiSuccessResponse<UserData>> loggedIn() {
        return ResponseEntity
                .ok()
                .body(ApiSuccessResponse.<UserData>builder()
                        .data(authService.isLoggedIn())
                        .message("User is authenticated")
                        .path("/api/v1/auth/logged_in")
                        .build()
                );
        
    }
    
}
