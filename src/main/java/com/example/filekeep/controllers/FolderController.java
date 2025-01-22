package com.example.filekeep.controllers;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.filekeep.dtos.NewFolderDto;
import com.example.filekeep.enums.Status;
import com.example.filekeep.models.Folder;
import com.example.filekeep.reponses.ApiResponse;
import com.example.filekeep.services.FolderService;

@RestController
@RequestMapping("/api/v1")
public class FolderController {
    private final FolderService folderService;

    public FolderController(FolderService folderService){
        this.folderService = folderService;
    }


    @GetMapping("/root")
    public ResponseEntity<ApiResponse<Folder>> getRoot(){
        return ResponseEntity
                .ok(ApiResponse.<Folder>builder()
                .message("Root successfully fetched")
                .data(folderService.getRoot())
                .path("/api/v1/root")
                .status(Status.SUCCESS)
                .build()
                );
    }

    @PostMapping("/create_folder")
    public ResponseEntity<ApiResponse<Folder>> createFolder(@RequestBody NewFolderDto newFolder){
        return ResponseEntity
        .created(URI.create("/api/v1/create_folder"))
        .body(ApiResponse.<Folder>builder()
            .message("Folder created!")
            .data(folderService.createFolder(newFolder))
            .path("/api/v1/create_folder")
            .status(Status.SUCCESS)
            .build()
            );
    }
}
