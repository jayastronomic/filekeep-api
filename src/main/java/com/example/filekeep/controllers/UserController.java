package com.example.filekeep.controllers;

import com.example.filekeep.models.User;
import com.example.filekeep.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
   public ResponseEntity<List<User>> findAll(){
        return  ResponseEntity.ok(this.userService.findAll());
    }
}
