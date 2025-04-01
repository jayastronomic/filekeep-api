package com.example.filekeep.dtos;

import jakarta.validation.constraints.NotBlank;

public record SyncData(@NotBlank(message = "Folder path cannot be blank") String folderPath) {}
