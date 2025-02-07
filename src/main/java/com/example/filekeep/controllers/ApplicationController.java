package com.example.filekeep.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.filekeep.repositories.FileRepository;
import com.example.filekeep.repositories.UserRepository;

@RestController
@RequestMapping("/delete_resources")
public class ApplicationController {
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private int called = 2;

    public ApplicationController(UserRepository userRepository, FileRepository fileRepository){
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
    }


    @DeleteMapping
    public ResponseEntity<String> deleteResources(){
        this.called = this.called + 1;
        this.fileRepository.deleteAll();
        this.userRepository.deleteAll();
        return ResponseEntity.ok("Resources deleted." + this.called );
    }
    
}
