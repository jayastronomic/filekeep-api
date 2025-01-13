package com.example.filekeep.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "File size cannot be blank")
    private long size;

    @Column(nullable = false)
    @NotBlank(message = "MIME type cannot be blank")
    private long mimeType;
}
