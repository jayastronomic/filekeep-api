package com.example.filekeep.repositories;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.filekeep.models.RootDirectory;

public interface RootDirectoryRepository extends JpaRepository<RootDirectory, UUID> {}
