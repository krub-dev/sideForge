package com.sideforge.service.impl;

import com.sideforge.dto.scene.*;
import com.sideforge.model.*;
import com.sideforge.repository.*;
import com.sideforge.service.interfaces.SceneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
public class SceneServiceImpl implements SceneService {

    private final SceneRepository sceneRepository;
    private final UserRepository userRepository;
    private final DesignRepository designRepository;

    @Autowired
    public SceneServiceImpl(SceneRepository sceneRepository, UserRepository userRepository, DesignRepository designRepository) {
        this.sceneRepository = sceneRepository;
        this.userRepository = userRepository;
        this.designRepository = designRepository;
    }

    // Create a new scene from SceneRequestDTO
    @Override
    @Transactional
    public SceneResponseDTO createScene(SceneRequestDTO dto) {
        User owner = userRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("Owner not found: " + dto.getOwnerId()));
        Design design = designRepository.findById(dto.getDesignId())
                .orElseThrow(() -> new IllegalArgumentException("Design not found: " + dto.getDesignId()));

        Scene scene = Scene.builder()
                .name(dto.getName())
                .lightingConfigJson(dto.getLightingConfigJson())
                .cameraConfigJson(dto.getCameraConfigJson())
                .thumbnail(dto.getThumbnail())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .owner(owner)
                .design(design)
                .build();

        Scene saved = sceneRepository.save(scene);
        return toResponseDTO(saved);
    }

    // Get a scene by its ID
    @Override
    public SceneResponseDTO getSceneById(Long id) {
        return sceneRepository.findById(id)
                .map(SceneServiceImpl::toResponseDTO)
                .orElse(null);
    }

    // Update a scene by its ID (PATCH: only not null fields are updated)
    @Override
    @Transactional
    public SceneResponseDTO updateScene(Long id, SceneUpdateDTO dto) {
        Scene scene = sceneRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Scene not found: " + id));

        if (dto.getName() != null) scene.setName(dto.getName());
        if (dto.getLightingConfigJson() != null) scene.setLightingConfigJson(dto.getLightingConfigJson());
        if (dto.getCameraConfigJson() != null) scene.setCameraConfigJson(dto.getCameraConfigJson());
        if (dto.getThumbnail() != null) scene.setThumbnail(dto.getThumbnail());
        if (dto.getCreatedAt() != null) scene.setCreatedAt(dto.getCreatedAt());
        if (dto.getUpdatedAt() != null) scene.setUpdatedAt(dto.getUpdatedAt());
        if (dto.getOwnerId() != null) {
            User owner = userRepository.findById(dto.getOwnerId())
                    .orElseThrow(() -> new IllegalArgumentException("Owner not found: " + dto.getOwnerId()));
            scene.setOwner(owner);
        }
        if (dto.getDesignId() != null) {
            Design design = designRepository.findById(dto.getDesignId())
                    .orElseThrow(() -> new IllegalArgumentException("Design not found: " + dto.getDesignId()));
            scene.setDesign(design);
        }

        Scene saved = sceneRepository.save(scene);
        return toResponseDTO(saved);
    }

    // Delete a scene by its ID
    @Override
    @Transactional
    public void deleteScene(Long id) {
        sceneRepository.deleteById(id);
    }

    // Get all scenes paginated
    @Override
    public Page<SceneResponseDTO> getAllScenes(Pageable pageable) {
        return sceneRepository.findAll(pageable).map(SceneServiceImpl::toResponseDTO);
    }

    // Get all scenes belonging to an owner (paginated)
    @Override
    public Page<SceneResponseDTO> getScenesByOwnerId(Long ownerId, Pageable pageable) {
        return sceneRepository.findByOwner_Id(ownerId, pageable).map(SceneServiceImpl::toResponseDTO);
    }

    // Get a scene by name and owner
    @Override
    public SceneResponseDTO getSceneByNameAndOwner(String name, Long ownerId) {
        return sceneRepository.findByNameAndOwner_Id(name, ownerId)
                .map(SceneServiceImpl::toResponseDTO)
                .orElse(null);
    }

    // Get all scenes created between two dates (paginated)
    @Override
    public Page<SceneResponseDTO> getScenesCreatedBetween(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return sceneRepository.findAllByCreatedAtBetween(start, end, pageable)
                .map(SceneServiceImpl::toResponseDTO);
    }

    // Count scenes by owner
    @Override
    public long countScenesByOwner(Long ownerId) {
        return sceneRepository.countByOwner_Id(ownerId);
    }

    // Helper to map Scene to SceneResponseDTO
    private static SceneResponseDTO toResponseDTO(Scene scene) {
        if (scene == null) return null;
        return SceneResponseDTO.builder()
                .id(scene.getId())
                .name(scene.getName())
                .lightingConfigJson(scene.getLightingConfigJson())
                .cameraConfigJson(scene.getCameraConfigJson())
                .thumbnail(scene.getThumbnail())
                .createdAt(scene.getCreatedAt())
                .updatedAt(scene.getUpdatedAt())
                .ownerId(scene.getOwner() != null ? scene.getOwner().getId() : null)
                .designId(scene.getDesign() != null ? scene.getDesign().getId() : null)
                .build();
    }
}