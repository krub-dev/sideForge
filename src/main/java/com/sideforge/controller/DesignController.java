package com.sideforge.controller;

import com.sideforge.dto.design.*;
import com.sideforge.service.interfaces.DesignService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/designs")
@Validated
public class DesignController {

    @Autowired
    private DesignService designService;

    @PostMapping
    @Operation(summary = "Create design", description = "Creates a new design and returns it.")
    public ResponseEntity<DesignResponseDTO> createDesign(@Valid @RequestBody DesignRequestDTO dto) {
        DesignResponseDTO created = designService.createDesign(dto);
        URI location = URI.create("/api/designs/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get design by ID", description = "Returns the design with the given ID.")
    public ResponseEntity<DesignResponseDTO> getDesignById(@PathVariable @Positive Long id) {
        DesignResponseDTO dto = designService.getDesignById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @GetMapping
    @Operation(summary = "Get all designs", description = "Returns a list of all designs.")
    public ResponseEntity<List<DesignResponseDTO>> getAllDesigns() {
        return ResponseEntity.ok(designService.getAllDesigns());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update design", description = "Updates an existing design.")
    public ResponseEntity<DesignResponseDTO> updateDesign(@PathVariable @Positive Long id, @Valid @RequestBody DesignUpdateDTO dto) {
        DesignResponseDTO updated = designService.updateDesign(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete design", description = "Deletes a design by its ID.")
    public ResponseEntity<Void> deleteDesign(@PathVariable @Positive Long id) {
        designService.deleteDesign(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-asset/{assetId}")
    @Operation(summary = "Get design by assetId", description = "Returns the design associated with the given assetId.")
    public ResponseEntity<DesignResponseDTO> getDesignByAssetId(@PathVariable @Positive Long assetId) {
        DesignResponseDTO dto = designService.getDesignByAssetId(assetId);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @GetMapping("/page")
    @Operation(summary = "Get paginated designs", description = "Returns a page of designs.")
    public ResponseEntity<Page<DesignResponseDTO>> getDesignsPage(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @PositiveOrZero int size,
            @RequestParam(defaultValue = "id,asc") String[] sort
    ) {
        Sort sortObj = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageable = PageRequest.of(page, size, sortObj);
        return ResponseEntity.ok(designService.getDesignsPage(pageable));
    }

    @GetMapping("/by-assets")
    @Operation(summary = "Get designs by assetIds (paginated)", description = "Returns a page of designs filtered by assetIds.")
    public ResponseEntity<Page<DesignResponseDTO>> getDesignsByAssetIds(
            @RequestParam List<Long> assetIds,
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @PositiveOrZero int size,
            @RequestParam(defaultValue = "id,asc") String[] sort
    ) {
        Sort sortObj = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageable = PageRequest.of(page, size, sortObj);
        return ResponseEntity.ok(designService.getDesignsByAssetIds(assetIds, pageable));
    }
}
