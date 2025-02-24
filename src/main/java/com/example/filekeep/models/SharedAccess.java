package com.example.filekeep.models;

import com.example.filekeep.enums.AccessType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name ="shared_access",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"folder_id", "collaborator_id"}),
        @UniqueConstraint(columnNames = {"file_id", "collaborator_id"})
    }
)
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
    @JoinColumn(name = "collaborator_id", nullable = false)
    private User collaborator;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccessType accessType; // Defines permission type (VIEW, EDIT, etc.)


    public SharedAccess(User owner, User collaborator, Folder folder, AccessType accessType) {
        this.file = null;
        this.collaborator = collaborator;
        this.owner = owner;
        this.folder = folder;
        this.accessType = accessType;
    }

    public SharedAccess(User owner, User collaborator, File file, AccessType accessType) {
        this.file = file;
        this.owner = owner;
        this.collaborator = collaborator;
        this.folder = null;
        this.accessType = accessType;
    }
}
