package com.example.filekeep.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.filekeep.models.FileAccess;

public interface FileAccessRepository extends JpaRepository<FileAccess, UUID> {}
