package com.sideforge.service.impl;

import com.sideforge.dto.asset.*;
import com.sideforge.model.Asset;
import com.sideforge.repository.AssetRepository;
import com.sideforge.service.interfaces.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;

    @Autowired
    public AssetServiceImpl(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    // Create a new asset from AssetRequestDTO
    @Override
    @Transactional
    public AssetResponseDTO createAsset(AssetRequestDTO assetRequestDTO) {
        Asset asset = Asset.builder()
                .name(assetRequestDTO.getName())
                .description(assetRequestDTO.getDescription())
                .glbPath(assetRequestDTO.getGlbPath())
                .thumbnailDefault(assetRequestDTO.getThumbnailDefault())
                .partsConfigJson(assetRequestDTO.getPartsConfigJson())
                .build();

        Asset saved = assetRepository.save(asset);
        return toResponseDTO(saved);
    }

    // Get a single asset by its ID
    @Override
    public AssetResponseDTO getAssetById(Long id) {
        Optional<Asset> asset = assetRepository.findById(id);
        return asset.map(AssetServiceImpl::toResponseDTO).orElse(null);
    }

    // Get all assets as a list
    @Override
    public List<AssetResponseDTO> getAllAssets() {
        return assetRepository.findAll()
                .stream()
                .map(AssetServiceImpl::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Update an asset by its ID
    @Override
    @Transactional
    public AssetResponseDTO updateAsset(Long id, AssetUpdateDTO assetUpdateDTO) {
        Optional<Asset> assetOpt = assetRepository.findById(id);
        if (assetOpt.isEmpty()) {
            return null;
        }
        Asset asset = assetOpt.get();
        asset.setName(assetUpdateDTO.getName());
        asset.setDescription(assetUpdateDTO.getDescription());
        asset.setGlbPath(assetUpdateDTO.getGlbPath());
        asset.setThumbnailDefault(assetUpdateDTO.getThumbnailDefault());
        asset.setPartsConfigJson(assetUpdateDTO.getPartsConfigJson());

        Asset saved = assetRepository.save(asset);
        return toResponseDTO(saved);
    }

    // Delete an asset by its ID
    @Override
    @Transactional
    public void deleteAsset(Long id) {
        assetRepository.deleteById(id);
    }

    // Get a paginated list of assets
    @Override
    public Page<AssetResponseDTO> getAssetsPage(Pageable pageable) {
        return assetRepository.findAll(pageable)
                .map(AssetServiceImpl::toResponseDTO);
    }

    // Paginated: Find assets by name (case-insensitive)
    @Override
    public Page<AssetResponseDTO> findAssetsByName(String name, Pageable pageable) {
        return assetRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(AssetServiceImpl::toResponseDTO);
    }

    // Helper to map Asset to AssetResponseDTO
    private static AssetResponseDTO toResponseDTO(Asset asset) {
        if (asset == null) {
            return null;
        }
        return AssetResponseDTO.builder()
                .id(asset.getId())
                .name(asset.getName())
                .description(asset.getDescription())
                .glbPath(asset.getGlbPath())
                .thumbnailDefault(asset.getThumbnailDefault())
                .partsConfigJson(asset.getPartsConfigJson())
                .build();
    }
}
