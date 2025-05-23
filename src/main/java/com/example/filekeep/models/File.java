package com.example.filekeep.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class File extends Asset<File> {

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(nullable = false)
    private long size;

    @Column(nullable = false)
    private String mimeType;

    @Column(nullable = false, unique = true)
    private String fileKey;

    @ManyToOne
    @JoinColumn(name ="user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "folder_id", nullable = true)
    private Folder folder;

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<SharedAccess> collaborators  = new ArrayList<>();

    @OneToOne(mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY )
    private ShareableLink shareableLink;

    public String getFileRemoteFilePath(){
        if(this.folder.getFolderName().equals("home")) return this.fileName;
        StringBuilder fullPath = new StringBuilder();
        Folder parentFolder = this.folder;
        while(parentFolder != null){
            fullPath.insert(0, parentFolder.getFolderName() + "/");
            parentFolder = parentFolder.getParentFolder();
        }
        return "/" + fullPath.toString().replaceFirst("^home/", "") + this.fileName;
    }
}
