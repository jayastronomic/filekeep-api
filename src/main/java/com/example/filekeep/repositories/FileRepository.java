package com.example.filekeep.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.filekeep.models.File;
import com.example.filekeep.models.Folder;

@Repository
public interface FileRepository extends JpaRepository<File, UUID> {
    List<File> findByUserId(UUID userId);
    boolean existsByUserIdAndFileName(UUID userId, String fileName);
    Optional<File> findByFileKey(String fileKey);
    File findByFileNameAndFolder(String fileName, Folder folder);;
}
