package com.sideforge.repository;

import com.sideforge.model.Scene;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SceneRepository extends JpaRepository<Scene, Long> {
    // Paginated version: Find all scenes by owner user ID with pagination and sorting
    // Scene history or user scenes dashboard
    Page<Scene> findByOwner_Id(Long ownerId, Pageable pageable);

    // Find scene by name and owner user ID
    Optional<Scene> findByNameAndOwner_Id(String name, Long ownerId);

    // Paginated: Find all scenes created between two dates with pagination and sorting
    Page<Scene> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    // Count scenes by owner user ID
    long countByOwner_Id(Long ownerId);
}
