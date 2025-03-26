package com.example.filekeep.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record NewFolderData( 
    @NotBlank(message = "Parent folder name cannot be blank") String parentName, 
    @NotBlank(message = "Folder name cannot be blank") String folderName,
    @NotBlank(message = "Parent id cannot be blank") UUID parentFolderId
    ) {}
    