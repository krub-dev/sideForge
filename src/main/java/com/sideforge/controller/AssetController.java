package com.sideforge.controller;

import com.sideforge.dto.asset.*;
import com.sideforge.service.interfaces.AssetService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/assets")
@Validated
public class AssetController {

    @Autowired
    private AssetService assetService;

    @GetMapping
    @Operation(summary = "Get all assets", description = "Returns a list of all assets.")
    public ResponseEntity<List<AssetResponseDTO>> getAllAssets() {
        return ResponseEntity.ok(assetService.getAllAssets());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get asset by ID", description = "Returns the details of an asset by its ID.")
    public ResponseEntity<AssetResponseDTO> getAssetById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(assetService.getAssetById(id));
    }

    @PostMapping
    @Operation(summary = "Create asset", description = "Creates a new asset and returns it.")
    public ResponseEntity<AssetResponseDTO> createAsset(@Valid @RequestBody AssetRequestDTO body) {
        AssetResponseDTO created = assetService.createAsset(body);
        URI location = URI.create("/api/assets/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update asset", description = "Updates the data of an existing asset.")
    public ResponseEntity<AssetResponseDTO> updateAsset(@PathVariable @Positive Long id, @Valid @RequestBody AssetUpdateDTO body) {
        return ResponseEntity.ok(assetService.updateAsset(id, body));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete asset", description = "Deletes an asset by its ID.")
    public ResponseEntity<Void> deleteAsset(@PathVariable @Positive Long id) {
        assetService.deleteAsset(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/page")
    @Operation(summary = "Get paginated assets", description = "Returns a paginated list of assets.")
    public ResponseEntity<Page<AssetResponseDTO>> getAssetsPage(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @PositiveOrZero int size,
            @RequestParam(defaultValue = "id,asc") String[] sort
    ) {
        Sort sortObj = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageable = PageRequest.of(page, size, sortObj);
        return ResponseEntity.ok(assetService.getAssetsPage(pageable));
    }

    @GetMapping("/search")
    @Operation(summary = "Search assets by name (paginated)",
            description = "Returns a paginated list of assets whose name contains the given string (case-insensitive)."
    )
    public ResponseEntity<Page<AssetResponseDTO>> findAssetsByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @PositiveOrZero int size,
            @RequestParam(defaultValue = "id,asc") String[] sort
    ) {
        Sort sortObj = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageable = PageRequest.of(page, size, sortObj);
        return ResponseEntity.ok(assetService.findAssetsByName(name, pageable));
    }
}

