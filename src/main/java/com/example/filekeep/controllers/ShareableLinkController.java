package com.example.filekeep.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.filekeep.dtos.FileData;
import com.example.filekeep.dtos.NewShareableLinkData;
import com.example.filekeep.dtos.ShareableFileData;
import com.example.filekeep.dtos.ShareableFolderData;
import com.example.filekeep.dtos.ShareableLinkData;
import com.example.filekeep.dtos.UpdateShareableLinkData;
import com.example.filekeep.reponses.ApiSuccessResponse;
import com.example.filekeep.services.ShareableLinkService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



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

    @GetMapping("/file")
    public ResponseEntity<ApiSuccessResponse<ShareableFileData>> getShareableFile(@RequestParam("token") String token) {
        ShareableFileData fileData = shareableLinkService.getShareableFile(token); 
        return ResponseEntity
                .ok()
                .body(ApiSuccessResponse.<ShareableFileData>builder()
                        .data(fileData)
                        .path("/api/v1/shareable_links/file")
                        .message("Shareable File data fetched.")
                        .build()
                );            
   }

    @PutMapping("/file")
    public ResponseEntity<ApiSuccessResponse<FileData>> updateFileShareableLinkAccess(@RequestParam("token") String token, @Valid @RequestBody UpdateShareableLinkData linkData) {
        return ResponseEntity
                .ok()
                .body(ApiSuccessResponse.<FileData>builder()
                        .data(shareableLinkService.updateFileShareableLinkAccess(token, linkData))
                        .path("/api/v1/shareable_links/file")
                        .message("ShareableLink Access updated")
                        .build()
                );            
    }

    @GetMapping("/folder")
    public ResponseEntity<ApiSuccessResponse<ShareableFolderData>> getShareableFolder(@RequestParam("token") String token) {
        return ResponseEntity
                .ok()
                .body(ApiSuccessResponse.<ShareableFolderData>builder()
                        .data(shareableLinkService.getShareableFolder(token))
                        .path("/api/v1/shareable_links/folder")
                        .message("Shareable Folder data fetched.")
                        .build()
                );            
    }
}
