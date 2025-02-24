package com.example.filekeep.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.filekeep.models.SharedAccess;

@Repository
public interface SharedAccessRepository extends JpaRepository<SharedAccess, UUID> {
    // Finds all SharedAccess where the user is either an owner or a collaborator
    List<SharedAccess> findAllByOwnerIdOrCollaboratorId(UUID ownerId, UUID collaboratorId);
}
