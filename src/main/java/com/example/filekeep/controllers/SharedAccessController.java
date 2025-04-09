package com.example.filekeep.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.filekeep.dtos.SharedAccessData;
import com.example.filekeep.reponses.ApiSuccessResponse;
import com.example.filekeep.services.SharedAccessService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/shared")
@RequiredArgsConstructor
public class SharedAccessController {
    private final SharedAccessService sharedAccessService;

    @GetMapping
    public ResponseEntity<ApiSuccessResponse<List<SharedAccessData>>> getSharedAssets() {
        return ResponseEntity
                        .ok()
                        .body(ApiSuccessResponse.<List<SharedAccessData>>builder()
                        .data(sharedAccessService.getSharedAssets())
                        .message("Successfully fetched shared assets")
                        .build()
                        );
    }  
}
