package com.example.filekeep.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.filekeep.models.Folder;

public interface FolderRepository extends JpaRepository<Folder, UUID> {
    Optional<Folder> getFolderByUserIdAndFolderName(UUID userId, String folderName);
}
