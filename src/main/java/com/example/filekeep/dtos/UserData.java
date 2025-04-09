package com.example.filekeep.dtos;

import java.util.UUID;

import com.example.filekeep.models.Folder;
import com.example.filekeep.models.User;

import lombok.Getter;

@Getter
public class UserData {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private UUID rootFolderId;

    public UserData(User user, Folder folder){
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName  = user.getFirstName();
        this.lastName = user.getLastName();
        this.rootFolderId = folder.getId();
    }
}
