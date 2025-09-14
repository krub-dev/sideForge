package com.sideforge.service.impl;

import com.sideforge.dto.asset.*;
import com.sideforge.exception.ResourceNotFoundException;
import com.sideforge.model.Asset;
import com.sideforge.repository.AssetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssetServiceImplTest {

    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private AssetServiceImpl assetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAsset_success() {
        AssetRequestDTO dto = AssetRequestDTO.builder()
                .name("Mug")
                .description("White mug")
                .glbPath("mug.glb")
                .thumbnailDefault("mug.png")
                .partsConfigJson("{\"color\":\"white\"}")
                .build();

        Asset asset = Asset.builder()
                .id(1L)
                .name("Mug")
                .description("White mug")
                .glbPath("mug.glb")
                .thumbnailDefault("mug.png")
                .partsConfigJson("{\"color\":\"white\"}")
                .build();

        when(assetRepository.save(any(Asset.class))).thenReturn(asset);

        AssetResponseDTO response = assetService.createAsset(dto);

        assertEquals("Mug", response.getName());
        assertEquals("White mug", response.getDescription());
        assertEquals("mug.glb", response.getGlbPath());
        assertEquals("mug.png", response.getThumbnailDefault());
        assertEquals("{\"color\":\"white\"}", response.getPartsConfigJson());
    }

    @Test
    void getAssetById_found() {
        Asset asset = Asset.builder()
                .id(1L)
                .name("Mug")
                .description("White mug")
                .glbPath("mug.glb")
                .thumbnailDefault("mug.png")
                .partsConfigJson("{}")
                .build();

        when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));

        AssetResponseDTO dto = assetService.getAssetById(1L);

        assertEquals(1L, dto.getId());
        assertEquals("Mug", dto.getName());
    }

    @Test
    void getAssetById_notFound() {
        when(assetRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> assetService.getAssetById(2L));
    }

    @Test
    void getAllAssets_success() {
        List<Asset> assets = Arrays.asList(
                Asset.builder().id(1L).name("A").glbPath("a.glb").build(),
                Asset.builder().id(2L).name("B").glbPath("b.glb").build()
        );
        when(assetRepository.findAll()).thenReturn(assets);

        List<AssetResponseDTO> result = assetService.getAllAssets();

        assertEquals(2, result.size());
        assertEquals("A", result.get(0).getName());
        assertEquals("B", result.get(1).getName());
    }

    @Test
    void updateAsset_success() {
        AssetUpdateDTO dto = AssetUpdateDTO.builder()
                .name("Updated mug")
                .description("Updated description")
                .glbPath("mug2.glb")
                .thumbnailDefault("mug2.png")
                .partsConfigJson("{\"color\":\"blue\"}")
                .build();

        Asset asset = Asset.builder()
                .id(1L)
                .name("Mug")
                .description("White mug")
                .glbPath("mug.glb")
                .thumbnailDefault("mug.png")
                .partsConfigJson("{}")
                .build();

        when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));
        when(assetRepository.save(any(Asset.class))).thenReturn(asset);

        AssetResponseDTO response = assetService.updateAsset(1L, dto);

        assertEquals("Updated mug", response.getName());
        assertEquals("Updated description", response.getDescription());
        assertEquals("mug2.glb", response.getGlbPath());
        assertEquals("mug2.png", response.getThumbnailDefault());
        assertEquals("{\"color\":\"blue\"}", response.getPartsConfigJson());
    }

    @Test
    void updateAsset_notFound() {
        AssetUpdateDTO dto = AssetUpdateDTO.builder().build();
        when(assetRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> assetService.updateAsset(1L, dto));
    }

    @Test
    void deleteAsset_success() {
        Asset asset = Asset.builder().id(1L).build();
        when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));
        doNothing().when(assetRepository).delete(asset);

        assertDoesNotThrow(() -> assetService.deleteAsset(1L));
        verify(assetRepository).delete(asset);
    }

    @Test
    void deleteAsset_notFound() {
        when(assetRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> assetService.deleteAsset(1L));
    }

    @Test
    void getAssetsPage_success() {
        Asset asset = Asset.builder().id(1L).name("PageAsset").glbPath("p.glb").build();
        Page<Asset> page = new PageImpl<>(List.of(asset));
        when(assetRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<AssetResponseDTO> result = assetService.getAssetsPage(PageRequest.of(0, 10));
        assertEquals(1, result.getTotalElements());
        assertEquals("PageAsset", result.getContent().get(0).getName());
    }

    @Test
    void findAssetsByName_success() {
        Asset asset = Asset.builder().id(1L).name("Mug").glbPath("mug.glb").build();
        Page<Asset> page = new PageImpl<>(List.of(asset));
        when(assetRepository.findByNameContainingIgnoreCase(eq("mug"), any(Pageable.class))).thenReturn(page);

        Page<AssetResponseDTO> result = assetService.findAssetsByName("mug", PageRequest.of(0, 10));
        assertEquals(1, result.getTotalElements());
        assertEquals("Mug", result.getContent().get(0).getName());
    }
}