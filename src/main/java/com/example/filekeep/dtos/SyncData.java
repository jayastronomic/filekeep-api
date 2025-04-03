package com.example.filekeep.dtos;

import org.springframework.web.multipart.MultipartFile;


public record SyncData(MultipartFile[] files) {}
