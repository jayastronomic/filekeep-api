package com.example.filekeep.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.filekeep.models.SharedAccess;
import com.example.filekeep.models.User;

@Repository
public interface SharedAccessRepository extends JpaRepository<SharedAccess, UUID> {
    List<SharedAccess> findAllByUser(User currentUser);
}
