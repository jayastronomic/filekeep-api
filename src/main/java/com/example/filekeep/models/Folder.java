package com.example.filekeep.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "folders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Folder extends Asset<Folder> {
    @Column(nullable = false)
    @NotBlank(message = "Folder name cannot be blank")
    private String folderName;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name ="user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "parent_folder_id")
    private Folder parentFolder;

    @OneToMany(mappedBy = "parentFolder", orphanRemoval = true, fetch = FetchType.EAGER)
    private final List<Folder> subFolders = new ArrayList<>();

    @OneToMany(mappedBy = "folder", orphanRemoval = true, cascade = CascadeType.ALL)
    private final List<File> files = new ArrayList<>();

    @OneToMany(mappedBy = "folder",  orphanRemoval = true, cascade = CascadeType.ALL)
    private final List<SharedAccess> collaborators = new ArrayList<>();

    @OneToOne(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY )
    private ShareableLink shareableLink;


    public Folder getFolderByName(String existingFolder){
       return this.subFolders.stream()
                        .filter(f -> f.getFolderName().equals(existingFolder))
                        .findFirst()
                        .orElse(null);
                        
    }

    public Folder(String folderName, User user){
        this.folderName = folderName;
        this.user = user;
    }

    public Folder(String folderName, User user, Folder parentFolder){
        this.folderName = folderName;
        this.user = user;
        this.parentFolder = parentFolder;
    }
    
}
