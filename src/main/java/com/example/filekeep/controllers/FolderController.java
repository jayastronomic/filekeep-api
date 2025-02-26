package com.example.filekeep.controllers;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.filekeep.dtos.FolderData;
import com.example.filekeep.dtos.NewFolderData;
import com.example.filekeep.dtos.ShareData;
import com.example.filekeep.reponses.ApiSuccessResponse;
import com.example.filekeep.services.FolderService;
import com.example.filekeep.services.SharedAccessService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/folders")
public class FolderController {
    private final FolderService folderService;
    private final SharedAccessService sharedAccessService;

    @GetMapping("/{folderName}")
    public ResponseEntity<ApiSuccessResponse<FolderData>> getFolder(@PathVariable("folderName") String folderName){
        return ResponseEntity
                .ok(ApiSuccessResponse.<FolderData>builder()
                .message(folderName + " folder successfully fetched")
                .data(folderService.getFolder(folderName))
                .path("/api/v1/folders/" + folderName)
                .build()
                );
    }

    @PostMapping
    public ResponseEntity<ApiSuccessResponse<FolderData>> createFolder(@RequestBody NewFolderData newFolder){
        return ResponseEntity
        .created(URI.create("/api/v1/folders"))
        .body(ApiSuccessResponse.<FolderData>builder()
            .message("Folder created!")
            .data(folderService.createFolder(newFolder))
            .path("/api/v1/folders")
            .build()
            );
    }

    @Transactional
    @DeleteMapping("/{folderId}")
    public ResponseEntity<ApiSuccessResponse<String>> deleteFolder(@PathVariable("folderId") UUID folderId){
        return ResponseEntity
                .ok(ApiSuccessResponse.<String>builder()
                .message("Folder successfully deleted with id: " + folderId)
                .data(folderService.deleteFolder(folderId))
                .path("/api/v1/folders/" + folderId)
                .build()
                );
    }


    @PostMapping("/share")
    public ResponseEntity<ApiSuccessResponse<String>> shareFile(@RequestBody ShareData payload) {
        return ResponseEntity
                .ok()
                .body(
                    ApiSuccessResponse.<String>builder()
                    .data(sharedAccessService.shareFolder(payload))
                    .message("Successfully shared folder.")
                    .path("/api/v1/files/share_file")
                    .build()
                );
    }
}
