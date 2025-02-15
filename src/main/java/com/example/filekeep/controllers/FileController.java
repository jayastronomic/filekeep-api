package com.example.filekeep.controllers;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.filekeep.dtos.ShareFileData;
import com.example.filekeep.reponses.ApiSuccessResponse;
import com.example.filekeep.services.FileService;
import com.example.filekeep.services.SharedAccessService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/files")
public class FileController {
    private final FileService fileService;
    private final SharedAccessService sharedAccessService;

    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiSuccessResponse<String>> upload(@RequestParam("file") MultipartFile file, @RequestParam("folder_name") String folderName){
        return ResponseEntity
                .created(URI.create("/api/v1/files/upload"))
                .body(ApiSuccessResponse.<String>builder()
                .data(fileService.uploadFile(file, folderName))
                .message("File uploaded succesfully to " + folderName + " folder")
                .path("/api/v1/files/upload")
                .build()
                );
    }

    @GetMapping("{fileKey}")
    public ResponseEntity<byte[]> download(@PathVariable("fileKey") String fileKey){
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileKey + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileService.downloadFile(fileKey));
                
    }

    @DeleteMapping("{fileKey}")
    public ResponseEntity<ApiSuccessResponse<String>> deleteFile(@PathVariable("fileKey") String fileKey){
        return ResponseEntity
                .ok(
                    ApiSuccessResponse.<String>builder()
                    .data(fileService.deleteFile(fileKey))
                    .message("Files deleted succesfully.")
                    .path("/api/v1/files/" + fileKey)
                    .build()
                );
    }

    @PostMapping("/share")
    public ResponseEntity<ApiSuccessResponse<String>> shareFile(@RequestBody ShareFileData payload) {
        return ResponseEntity
                .ok()
                .body(
                    ApiSuccessResponse.<String>builder()
                    .data(sharedAccessService.shareFile(payload))
                    .message("Successfully shared file.")
                    .path("/api/v1/files/share_file")
                    .build()
                );
    }
}

