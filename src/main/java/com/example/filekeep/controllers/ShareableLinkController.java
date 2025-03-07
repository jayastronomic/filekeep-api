package com.example.filekeep.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.filekeep.dtos.NewShareableLinkData;
import com.example.filekeep.dtos.ShareableFileData;
import com.example.filekeep.dtos.ShareableLinkData;
import com.example.filekeep.reponses.ApiSuccessResponse;
import com.example.filekeep.services.ShareableLinkService;

import lombok.AllArgsConstructor;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<byte[]> getFile(@RequestParam("token") String token){
        ShareableFileData fileData = shareableLinkService.getShareableFile(token); 
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileData.getFile().getFileName() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, fileData.getFile().getMimeType())
                .body(fileData.getStream());             
    }
}
