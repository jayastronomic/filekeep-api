package com.example.filekeep.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.filekeep.dtos.NewShareableLinkData;
import com.example.filekeep.dtos.ShareableLinkData;
import com.example.filekeep.reponses.ApiSuccessResponse;
import com.example.filekeep.services.ShareableLinkService;

import lombok.AllArgsConstructor;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/shareable_links")
@AllArgsConstructor
public class ShareableLinkController {
    private final ShareableLinkService shareableLinkService;

    @PostMapping
    public ResponseEntity<ApiSuccessResponse<ShareableLinkData>> createShareableLink(@RequestBody NewShareableLinkData payload) {
        return ResponseEntity
                    .created(URI.create("/api/v1/shareable_links"))
                    .body(ApiSuccessResponse.<ShareableLinkData>builder()
                            .data(shareableLinkService.createShareableLink(payload))
                            .message("Link created.")
                            .build()
                    );
    }
}
