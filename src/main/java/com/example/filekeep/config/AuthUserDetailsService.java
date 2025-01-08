package com.example.filekeep.config;

import com.example.filekeep.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * This is the Auth User Details Service class, responsible for loading user details by their email during authentication.
 */
@Service
public class AuthUserDetailsService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    public AuthUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads user details by their email address
     * @param email The email address of the user to load.
     * @return UserDetails containing user information.
     * @throws UsernameNotFoundException If user with specified email is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new AuthUserDetails(userRepository.findByEmail(email).orElseThrow());
    }
}
