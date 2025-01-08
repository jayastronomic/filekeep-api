package com.example.filekeep.services;

import com.example.filekeep.config.AuthUserDetails;
import com.example.filekeep.jwt.JwtUtils;
import com.example.filekeep.models.User;
import com.example.filekeep.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder, JwtUtils jwtUtils, @Lazy AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    public String create(User userObject) {
        boolean exists = userRepository.existsByEmail(userObject.getEmail());
        if(exists) return null;
        userObject.setPassword(passwordEncoder.encode(userObject.getPassword()));
        String jwt = jwtUtils.generateTokenFromUsername(new AuthUserDetails(userObject));
        userRepository.save(userObject);
        return jwt;
    }
}
