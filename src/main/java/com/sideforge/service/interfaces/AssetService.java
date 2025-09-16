package com.sideforge.service.interfaces;

import com.sideforge.dto.asset.AssetRequestDTO;
import com.sideforge.dto.asset.AssetResponseDTO;
import com.sideforge.dto.asset.AssetUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface AssetService {
    // Create a new asset from AssetRequestDTO
    AssetResponseDTO createAsset(AssetRequestDTO assetRequestDTO);

    // Get a single asset by its ID
    AssetResponseDTO getAssetById(Long id);

    // Get all assets as a list
    List<AssetResponseDTO> getAllAssets();

    // Update an asset by its ID
    AssetResponseDTO updateAsset(Long id, AssetUpdateDTO assetUpdateDTO);

    // Delete an asset by its ID
    void deleteAsset(Long id);

    // Get a paginated list of assets
    Page<AssetResponseDTO> getAssetsPage(Pageable pageable);

    // Paginated: Find assets by name (case-insensitive)
    Page<AssetResponseDTO> findAssetsByName(String name, Pageable pageable);
}
