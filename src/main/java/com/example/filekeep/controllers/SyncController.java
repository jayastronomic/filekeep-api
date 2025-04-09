package com.example.filekeep.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.filekeep.reponses.ApiSuccessResponse;
import com.example.filekeep.services.SyncService;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1/sync")
@AllArgsConstructor
public class SyncController {
    private final SyncService syncService;

    @PostMapping
    public ResponseEntity<ApiSuccessResponse<Object>> sync(@RequestParam("files") MultipartFile[] files,
    @RequestParam(value = "directories", required = false) List<String> directories,
    @RequestHeader(value = "Authorization") String authorizationHeader) {
        return ResponseEntity.created(URI.create("/api/v1/sync"))
                            .header("Authorization", authorizationHeader)
                            .body((ApiSuccessResponse.builder()
                                .message("Synced")
                                .data(syncService.sync(files, directories))
                                .path("/api/v1/sync")
                                .build()
                                )
                            );
    }

    @GetMapping
    public ResponseEntity<ApiSuccessResponse<byte[]>> getRemoteFiles() {
        return ResponseEntity.ok((ApiSuccessResponse.<byte[]>builder()
                                .message("Synced")
                                .data(syncService.getZipData())
                                .path("/api/v1/sync")
                                .build()
                                )
                            );
    }
}