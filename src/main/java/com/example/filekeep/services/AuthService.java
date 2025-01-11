package com.example.filekeep.services;

import com.example.filekeep.config.AuthUserDetails;
import com.example.filekeep.exceptions.InvalidCredentialsException;
import com.example.filekeep.exceptions.UserAlreadyExistException;
import com.example.filekeep.jwt.JwtUtils;
import com.example.filekeep.models.User;
import com.example.filekeep.repositories.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService extends ApplicationService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder, JwtUtils jwtUtils, @Lazy AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    public String register(User payload) throws UserAlreadyExistException {
        boolean exists = userRepository.existsByEmail(payload.getEmail());
        if (exists) throw new UserAlreadyExistException(payload.getEmail());
        payload.setPassword(passwordEncoder.encode(payload.getPassword()));
        String username = payload.getEmail();
        String jwt = jwtUtils.generateTokenFromUsername(username);
        userRepository.save(payload);
        return jwt;
    }

    public String login(User payload){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(payload.getEmail(), payload.getPassword());
        try {
            Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            String username = ((AuthUserDetails) authentication.getPrincipal()).getUsername();
            return jwtUtils.generateTokenFromUsername(username);
        } catch (Exception e) {
            throw new InvalidCredentialsException();
        }
    }

    public User isLoggedIn(){
        return currentUser();
    }
}
