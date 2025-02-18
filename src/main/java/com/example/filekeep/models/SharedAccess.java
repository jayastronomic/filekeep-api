package com.example.filekeep.models;

import com.example.filekeep.enums.AccessType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name ="shared_access")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SharedAccess extends ApplicationEntity<SharedAccess> {

    @ManyToOne
    @JoinColumn(name = "file_id")
    private File file;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccessType accessType; // Defines permission type (VIEW, EDIT, etc.)


    public SharedAccess(User user, Folder folder, AccessType accessType) {
        this.file = null;
        this.user = user;
        this.folder = folder;
        this.accessType = accessType;
    }

    public SharedAccess(User user, File file, AccessType accessType) {
        this.file = file;
        this.user = user;
        this.folder = null;
        this.accessType = accessType;
    }
}
