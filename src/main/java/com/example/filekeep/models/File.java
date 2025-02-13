package com.example.filekeep.models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(
    name = "files",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"file_name", "folder_id"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class File extends ApplicationEntity<File> {
    @Column(name = "file_name", nullable = false)
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

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<FileAccess> fileAccessList = new ArrayList<>();

    public File build(MultipartFile file){
        this.setFileName(file.getOriginalFilename());
        this.setSize(file.getSize());
        this.setMimeType(file.getContentType());
        return this;
    }
}
