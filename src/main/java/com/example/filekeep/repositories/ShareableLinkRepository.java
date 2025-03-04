package com.example.filekeep.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.filekeep.models.ShareableLink;

@Repository
public interface ShareableLinkRepository extends JpaRepository<ShareableLink, UUID> {}
