package com.example.filekeep.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record NewFolderDto( 
    @NotBlank(message = "Parent folder ID cannot be blank") UUID parentId, 
    @NotBlank(message = "Folder name cannot be blank")String folderName) {}