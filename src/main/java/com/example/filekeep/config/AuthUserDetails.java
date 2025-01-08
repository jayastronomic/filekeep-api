package com.example.filekeep.config;
import com.example.filekeep.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

/**
 * AuthUserDetails class represents a custom User Details implementation for authentication purposes
 * in the application. It encapsulates user information and implements the user detail interface.
 */
public class AuthUserDetails implements UserDetails {
    private final User user;

    public AuthUserDetails(User user){
        this.user = user;
    }

    /**
     * Returns an empty set of granted authorities since the application does not use role-based
     * @return An empty set of GrantedAuthority objects
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet<>();
    }

    /**
     * Retrieves user password
     * @return The users password
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Retrieves user email, which serves as the username for authentication
     * @return The users email
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Indicates user account is not expired
     * @return True, indicating that the users account is not expired.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates user account is always not locked
     * @return True, indicating that the users account is not locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates user account credentials is not expired
     * @return True, indicating that the users account credentials are not expired.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates user account is enabled
     * @return True, indicating that the users account is enabled.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser() { return user; }

}
