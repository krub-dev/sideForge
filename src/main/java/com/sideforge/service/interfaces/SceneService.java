package com.sideforge.service.interfaces;

import com.sideforge.dto.scene.*;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;

public interface SceneService {
    // Create a new scene from SceneRequestDTO
    SceneResponseDTO createScene(SceneRequestDTO sceneRequestDTO);

    // Get a scene by its ID
    SceneResponseDTO getSceneById(Long id);

    // Update a scene by its ID
    SceneResponseDTO updateScene(Long id, SceneUpdateDTO sceneUpdateDTO);

    // Delete a scene by its ID
    void deleteScene(Long id);

    // Get all scenes paginated
    Page<SceneResponseDTO> getAllScenes(Pageable pageable);

    // Get all scenes belonging to an owner (paginated)
    Page<SceneResponseDTO> getScenesByOwnerId(Long ownerId, Pageable pageable);

    // Get a scene by name and owner
    SceneResponseDTO getSceneByNameAndOwner(String name, Long ownerId);

    // Get all scenes created between two dates (paginated)
    Page<SceneResponseDTO> getScenesCreatedBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    // Count scenes by owner
    long countScenesByOwner(Long ownerId);
}
