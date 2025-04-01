package com.example.filekeep.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.filekeep.models.Folder;

@Repository
public interface FolderRepository extends JpaRepository<Folder, UUID> {
    boolean existsByFolderName(String folderName);
    Optional<Folder> getByUserIdAndFolderName(UUID currentUserId, String homeFolder);

    @Query("SELECT f FROM Folder f WHERE f.user.id = :userId AND f.parentFolder IS NULL")
    Optional<Folder> getRootFolder(@Param("userId") UUID userId);
}