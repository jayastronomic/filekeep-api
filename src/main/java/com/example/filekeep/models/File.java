package com.example.filekeep.models;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class File extends ApplicationEntity<File> {
    @Column(nullable = false, unique = true)
    @NotBlank(message = "File name cannot be blank")
    private String fileName;

    @Column(nullable = false)
    @NotNull(message = "File size cannot be blank")
    private long size;

    @Column(nullable = false)
    @NotBlank(message = "MIME type cannot be blank")
    private String mimeType;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "File key cannot be blank")
    private String fileKey;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name ="user_id", nullable = false)
    private User user;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "folder_id", nullable = true)
    private Folder folder;

    public File build(MultipartFile file){
        this.setFileName(file.getOriginalFilename());
        this.setSize(file.getSize());
        this.setMimeType(file.getContentType());
        return this;
    }
}
