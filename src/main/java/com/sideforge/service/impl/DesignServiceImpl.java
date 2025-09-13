package com.sideforge.service.impl;

import com.sideforge.dto.design.*;
import com.sideforge.model.*;
import com.sideforge.repository.*;
import com.sideforge.service.interfaces.DesignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DesignServiceImpl implements DesignService {

    private final DesignRepository designRepository;
    private final AssetRepository assetRepository;

    @Autowired
    public DesignServiceImpl(DesignRepository designRepository, AssetRepository assetRepository) {
        this.designRepository = designRepository;
        this.assetRepository = assetRepository;
    }

    // Create a new design from DesignRequestDTO
    @Override
    @Transactional
    public DesignResponseDTO createDesign(DesignRequestDTO dto) {
        Asset asset = assetRepository.findById(dto.getAssetId())
                .orElseThrow(() -> new IllegalArgumentException("Asset not found: " + dto.getAssetId()));
        Design design = Design.builder()
                .name(dto.getName())
                .textureMapUrl(dto.getTextureMapUrl())
                .materialsJson(dto.getMaterialsJson())
                .partsColorsJson(dto.getPartsColorsJson())
                .logoConfigJson(dto.getLogoConfigJson())
                .textConfigJson(dto.getTextConfigJson())
                .asset(asset)
                .build();
        Design saved = designRepository.save(design);
        return toResponseDTO(saved);
    }

    // Get a design by its ID
    @Override
    public DesignResponseDTO getDesignById(Long id) {
        return designRepository.findById(id).map(DesignServiceImpl::toResponseDTO).orElse(null);
    }

    // Get all designs as a list
    @Override
    public List<DesignResponseDTO> getAllDesigns() {
        return designRepository.findAll().stream()
                .map(DesignServiceImpl::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Update a design by its ID
    @Override
    @Transactional
    public DesignResponseDTO updateDesign(Long id, DesignUpdateDTO dto) {
        Optional<Design> designOpt = designRepository.findById(id);
        if (designOpt.isEmpty()) {
            return null;
        }
        Design design = designOpt.get();

        if (dto.getName() != null) design.setName(dto.getName());
        if (dto.getTextureMapUrl() != null) design.setTextureMapUrl(dto.getTextureMapUrl());
        if (dto.getMaterialsJson() != null) design.setMaterialsJson(dto.getMaterialsJson());
        if (dto.getPartsColorsJson() != null) design.setPartsColorsJson(dto.getPartsColorsJson());
        if (dto.getLogoConfigJson() != null) design.setLogoConfigJson(dto.getLogoConfigJson());
        if (dto.getTextConfigJson() != null) design.setTextConfigJson(dto.getTextConfigJson());
        if (dto.getAssetId() != null) {
            Asset asset = assetRepository.findById(dto.getAssetId())
                    .orElseThrow(() -> new IllegalArgumentException("Asset not found: " + dto.getAssetId()));
            design.setAsset(asset);
        }
        Design saved = designRepository.save(design);
        return toResponseDTO(saved);
    }

    // Delete a design by its ID
    @Override
    @Transactional
    public void deleteDesign(Long id) {
        designRepository.deleteById(id);
    }

    // Get a design by asset ID (1:1 relation)
    @Override
    public DesignResponseDTO getDesignByAssetId(Long assetId) {
        return designRepository.findByAsset_Id(assetId)
                .map(DesignServiceImpl::toResponseDTO)
                .orElse(null);
    }

    // Get paginated designs filtered by asset IDs
    @Override
    public Page<DesignResponseDTO> getDesignsByAssetIds(List<Long> assetIds, Pageable pageable) {
        return designRepository.findAllByAsset_IdIn(assetIds, pageable)
                .map(DesignServiceImpl::toResponseDTO);
    }

    // Get paginated list of all designs
    @Override
    public Page<DesignResponseDTO> getDesignsPage(Pageable pageable) {
        return designRepository.findAll(pageable)
                .map(DesignServiceImpl::toResponseDTO);
    }

    // Helper to map Design to DesignResponseDTO
    private static DesignResponseDTO toResponseDTO(Design design) {
        if (design == null)
            return null;
        return DesignResponseDTO.builder()
                .id(design.getId())
                .name(design.getName())
                .textureMapUrl(design.getTextureMapUrl())
                .materialsJson(design.getMaterialsJson())
                .partsColorsJson(design.getPartsColorsJson())
                .logoConfigJson(design.getLogoConfigJson())
                .textConfigJson(design.getTextConfigJson())
                .assetId(design.getAsset() != null ? design.getAsset().getId() : null)
                .build();
    }
}
