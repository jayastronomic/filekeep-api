package com.example.filekeep.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.filekeep.models.File;

public interface FileRepository extends JpaRepository<File, UUID> {
    List<File> findByUserId(UUID userId);
}
