package com.example.filekeep.controllers;

import java.net.URI;

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
    public ResponseEntity<ApiResponse<String>> upload(@RequestParam("file") MultipartFile file){
        return ResponseEntity
                .created(URI.create("/api/v1/files/upload"))
                .body(ApiResponse.<String>builder()
                .data(fileService.uploadFile(file))
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
}
