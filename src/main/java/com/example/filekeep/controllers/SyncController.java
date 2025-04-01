package com.example.filekeep.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.filekeep.dtos.SyncData;
import com.example.filekeep.reponses.ApiSuccessResponse;
import com.example.filekeep.services.SyncService;
import org.springframework.http.ResponseEntity;


import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/sync")
@AllArgsConstructor
public class SyncController {
    private final SyncService syncService;

    @PostMapping
    public ResponseEntity<ApiSuccessResponse<Object>> sync(@RequestBody SyncData payload) {
        return ResponseEntity.ok(ApiSuccessResponse.builder()
                                .message("Synced")
                                .data(syncService.sync(payload))
                                .path("/api/v1/sync")
                                .build());
    }
}