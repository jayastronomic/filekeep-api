package com.example.filekeep.dtos;

import jakarta.validation.constraints.NotBlank;

public record NewFolderData( 
    @NotBlank(message = "Parent folder name cannot be blank") String parentName, 
    @NotBlank(message = "Folder name cannot be blank") String folderName) {}