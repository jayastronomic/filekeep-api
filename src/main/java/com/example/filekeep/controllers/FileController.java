package com.example.filekeep.controllers;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.filekeep.enums.Status;
import com.example.filekeep.models.File;
import com.example.filekeep.reponses.ApiResponse;
import com.example.filekeep.services.FileService;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {
    private final FileService fileService;


    public FileController(FileService fileService){
        this.fileService = fileService;
    }


    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<String>> upload(@RequestParam("file") MultipartFile file, @RequestParam("parent_id") UUID parentId){
        return ResponseEntity
                .created(URI.create("/api/v1/files/upload"))
                .body(ApiResponse.<String>builder()
                .data(fileService.uploadFile(file, parentId))
                .message("File uploaded succesfully")
                .path("/api/v1/files/upload")
                .status(Status.SUCCESS)
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

    @GetMapping
    public ResponseEntity<ApiResponse<List<File>>> getUserFiles(){
        return ResponseEntity
                .ok(
                    ApiResponse.<List<File>>builder()
                    .data(fileService.getUserFiles())
                    .message("Files loaded succesfully.")
                    .status(Status.SUCCESS)
                    .path("/api/v1/files")
                    .build()
                );
    }
}
