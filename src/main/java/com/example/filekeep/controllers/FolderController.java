package com.example.filekeep.controllers;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.filekeep.dtos.NewFolderDto;
import com.example.filekeep.enums.Status;
import com.example.filekeep.models.Folder;
import com.example.filekeep.reponses.ApiResponse;
import com.example.filekeep.services.FolderService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/v1/folders")
public class FolderController {
    private final FolderService folderService;

    public FolderController(FolderService folderService){
        this.folderService = folderService;
    }


    @GetMapping("/{folderName}")
    public ResponseEntity<ApiResponse<Folder>> getFolder(@PathVariable("folderName") String folderName){
        return ResponseEntity
                .ok(ApiResponse.<Folder>builder()
                .message(folderName + " folder successfully fetched")
                .data(folderService.getFolder(folderName))
                .path("/api/v1/folders/" + folderName)
                .status(Status.SUCCESS)
                .build()
                );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Folder>> createFolder(@RequestBody NewFolderDto newFolder){
        return ResponseEntity
        .created(URI.create("/api/v1/folders"))
        .body(ApiResponse.<Folder>builder()
            .message("Folder created!")
            .data(folderService.createFolder(newFolder))
            .path("/api/v1/folders")
            .status(Status.SUCCESS)
            .build()
            );
    }

    @Transactional
    @DeleteMapping("/{folderId}")
    public ResponseEntity<ApiResponse<String>> deleteFolder(@PathVariable("folderId") UUID folderId){
        return ResponseEntity
                .ok(ApiResponse.<String>builder()
                .message("Folder successfully deleted with id: " + folderId)
                .data(folderService.deleteFolder(folderId))
                .path("/api/v1/folders/" + folderId)
                .status(Status.SUCCESS)
                .build()
                );
    }
}
