package com.sideforge.controller;

import com.sideforge.dto.scene.*;
import com.sideforge.service.interfaces.SceneService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/scenes")
@Validated
public class SceneController {

    @Autowired
    private SceneService sceneService;

    @PostMapping
    @Operation(summary = "Create scene", description = "Creates a new scene and returns it.")
    public ResponseEntity<SceneResponseDTO> createScene(@Valid @RequestBody SceneRequestDTO dto) {
        SceneResponseDTO created = sceneService.createScene(dto);
        URI location = URI.create("/api/scenes/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get scene by ID", description = "Returns the details of a scene by its ID.")
    public ResponseEntity<SceneResponseDTO> getSceneById(@PathVariable @Positive Long id) {
        SceneResponseDTO dto = sceneService.getSceneById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update scene", description = "Updates the data of an existing scene.")
    public ResponseEntity<SceneResponseDTO> updateScene(@PathVariable @Positive Long id, @Valid @RequestBody SceneUpdateDTO dto) {
        SceneResponseDTO updated = sceneService.updateScene(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete scene", description = "Deletes a scene by its ID.")
    public ResponseEntity<Void> deleteScene(@PathVariable @Positive Long id) {
        sceneService.deleteScene(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/page")
    @Operation(summary = "Get paginated scenes", description = "Returns a paginated list of all scenes.")
    public ResponseEntity<Page<SceneResponseDTO>> getScenesPage(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @PositiveOrZero int size,
            @RequestParam(defaultValue = "id,asc") String[] sort
    ) {
        Sort sortObj = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageable = PageRequest.of(page, size, sortObj);
        return ResponseEntity.ok(sceneService.getAllScenes(pageable));
    }

    @GetMapping("/by-owner")
    @Operation(summary = "Get scenes by owner (paginated)", description = "Returns a paginated list of scenes belonging to an owner.")
    public ResponseEntity<Page<SceneResponseDTO>> getScenesByOwnerId(
            @RequestParam @Positive Long ownerId,
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @PositiveOrZero int size,
            @RequestParam(defaultValue = "id,asc") String[] sort
    ) {
        Sort sortObj = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageable = PageRequest.of(page, size, sortObj);
        return ResponseEntity.ok(sceneService.getScenesByOwnerId(ownerId, pageable));
    }

    @GetMapping("/by-name-and-owner")
    @Operation(summary = "Get scene by name and owner", description = "Returns a scene by its name and owner ID.")
    public ResponseEntity<SceneResponseDTO> getSceneByNameAndOwner(
            @RequestParam String name,
            @RequestParam @Positive Long ownerId
    ) {
        SceneResponseDTO dto = sceneService.getSceneByNameAndOwner(name, ownerId);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @GetMapping("/created-between")
    @Operation(summary = "Get scenes created between dates (paginated)", description = "Returns a paginated list of scenes created between two dates.")
    public ResponseEntity<Page<SceneResponseDTO>> getScenesCreatedBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @PositiveOrZero int size,
            @RequestParam(defaultValue = "id,asc") String[] sort
    ) {
        Sort sortObj = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageable = PageRequest.of(page, size, sortObj);
        return ResponseEntity.ok(sceneService.getScenesCreatedBetween(start, end, pageable));
    }

    @GetMapping("/count-by-owner")
    @Operation(summary = "Count scenes by owner", description = "Returns the number of scenes belonging to an owner.")
    public ResponseEntity<Long> countScenesByOwner(@RequestParam @Positive Long ownerId) {
        return ResponseEntity.ok(sceneService.countScenesByOwner(ownerId));
    }
}