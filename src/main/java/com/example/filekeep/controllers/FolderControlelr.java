package com.example.filekeep.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.filekeep.enums.Status;
import com.example.filekeep.models.Folder;
import com.example.filekeep.reponses.ApiResponse;
import com.example.filekeep.services.FolderService;

@RestController
@RequestMapping("/api/v1/root")
public class FolderControlelr {
    private final FolderService folderService;

    public FolderControlelr(FolderService folderService){
        this.folderService = folderService;
    }


    @GetMapping
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
}
