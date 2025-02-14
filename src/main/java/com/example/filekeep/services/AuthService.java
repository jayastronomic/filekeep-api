package com.example.filekeep.services;

import com.example.filekeep.config.AuthUserDetails;
import com.example.filekeep.dtos.LoginData;
import com.example.filekeep.dtos.UserData;
import com.example.filekeep.exceptions.InvalidCredentialsException;
import com.example.filekeep.exceptions.PasswordMismatchException;
import com.example.filekeep.exceptions.UserAlreadyExistException;
import com.example.filekeep.jwt.JwtUtils;
import com.example.filekeep.models.Folder;
import com.example.filekeep.models.User;
import com.example.filekeep.repositories.UserRepository;
import com.example.filekeep.requests.NewUserData;

import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthService extends ApplicationService{
    private final UserRepository userRepository;
    private final @Lazy PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

  
    public String register(NewUserData payload) throws UserAlreadyExistException {
        boolean exists = userRepository.existsByEmail(payload.email());
        if (exists) throw new UserAlreadyExistException(payload.email());
        if (!payload.password().equals(payload.passwordConfirmation())) throw new PasswordMismatchException();
        User newUser = new User(payload, passwordEncoder.encode(payload.password()));                   
        Folder root = new Folder();
        root.setFolderName("home");
        root.setUser(newUser);
        newUser = userRepository.save(newUser);
        return jwtUtils.generateTokenFromUsername(newUser.getEmail());
    }

    public String login(LoginData payload){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(payload.email(), payload.password());
        try {
            Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            String username = ((AuthUserDetails) authentication.getPrincipal()).getUsername();
            return jwtUtils.generateTokenFromUsername(username);
        } catch (Exception e) {
            throw new InvalidCredentialsException();
        }
    }

    public UserData isLoggedIn(){
        return new UserData(currentUser());
    }
}
