package com.example.filekeep.services;

import com.example.filekeep.config.AuthUserDetails;
import com.example.filekeep.dtos.NewUserDto;
import com.example.filekeep.exceptions.InvalidCredentialsException;
import com.example.filekeep.exceptions.PasswordMismatchException;
import com.example.filekeep.exceptions.UserAlreadyExistException;
import com.example.filekeep.jwt.JwtUtils;
import com.example.filekeep.models.Folder;
import com.example.filekeep.models.User;
import com.example.filekeep.repositories.UserRepository;

import java.util.ArrayList;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    public String register(NewUserDto payload) throws UserAlreadyExistException {
        boolean exists = userRepository.existsByEmail(payload.email());
        if (exists) throw new UserAlreadyExistException(payload.email());
        if (!payload.password().equals(payload.passwordConfirmation())) throw new PasswordMismatchException();
        User newUser = User.builder()
                            .email(payload.email())
                            .password(passwordEncoder.encode(payload.password()))
                            .build();
        Folder root = new Folder();
        root.setFolderName("/");
        root.setUser(newUser);
        newUser.getFolders().add(root);
        newUser = userRepository.save(newUser);
        String username = newUser.getEmail();
        return jwtUtils.generateTokenFromUsername(username);
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
