package com.example.filekeep.dtos;

import java.util.UUID;

import com.example.filekeep.models.User;

import lombok.Getter;

@Getter
public class UserData {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;

    public UserData(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName  = user.getEmail();
        this.lastName = user.getLastName();
    }
}
