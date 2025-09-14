package com.sideforge.service.impl;

import com.sideforge.dto.design.*;
import com.sideforge.exception.ResourceNotFoundException;
import com.sideforge.model.Asset;
import com.sideforge.model.Design;
import com.sideforge.repository.AssetRepository;
import com.sideforge.repository.DesignRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DesignServiceImplTest {

    @Mock
    private DesignRepository designRepository;
    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private DesignServiceImpl designService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createDesign_success() {
        DesignRequestDTO dto = DesignRequestDTO.builder()
                .name("Modern Mug Design")
                .textureMapUrl("texture.png")
                .materialsJson("{\"mat\":\"ceramic\"}")
                .partsColorsJson("{\"handle\":\"blue\"}")
                .logoConfigJson("{\"logo\":\"logo.png\"}")
                .textConfigJson("{\"text\":\"Hello\"}")
                .assetId(1L)
                .build();

        Asset asset = Asset.builder().id(1L).name("Mug").build();
        Design design = Design.builder()
                .id(10L)
                .name("Modern Mug Design")
                .textureMapUrl("texture.png")
                .materialsJson("{\"mat\":\"ceramic\"}")
                .partsColorsJson("{\"handle\":\"blue\"}")
                .logoConfigJson("{\"logo\":\"logo.png\"}")
                .textConfigJson("{\"text\":\"Hello\"}")
                .asset(asset)
                .build();

        when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));
        when(designRepository.save(any(Design.class))).thenReturn(design);

        DesignResponseDTO response = designService.createDesign(dto);

        assertEquals("Modern Mug Design", response.getName());
        assertEquals("texture.png", response.getTextureMapUrl());
        assertEquals(1L, response.getAssetId());
    }

    @Test
    void createDesign_assetNotFound() {
        DesignRequestDTO dto = DesignRequestDTO.builder().assetId(2L).build();
        when(assetRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> designService.createDesign(dto));
    }

    @Test
    void getDesignById_found() {
        Design design = Design.builder().id(5L).name("Classic Design").build();
        when(designRepository.findById(5L)).thenReturn(Optional.of(design));
        DesignResponseDTO dto = designService.getDesignById(5L);
        assertEquals(5L, dto.getId());
        assertEquals("Classic Design", dto.getName());
    }

    @Test
    void getDesignById_notFound() {
        when(designRepository.findById(6L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> designService.getDesignById(6L));
    }

    @Test
    void getAllDesigns_success() {
        List<Design> designs = Arrays.asList(
                Design.builder().id(1L).name("A").build(),
                Design.builder().id(2L).name("B").build()
        );
        when(designRepository.findAll()).thenReturn(designs);

        List<DesignResponseDTO> result = designService.getAllDesigns();

        assertEquals(2, result.size());
        assertEquals("A", result.get(0).getName());
        assertEquals("B", result.get(1).getName());
    }

    @Test
    void updateDesign_success() {
        DesignUpdateDTO dto = DesignUpdateDTO.builder()
                .name("Updated Design")
                .textureMapUrl("updated.png")
                .materialsJson("{\"mat\":\"glass\"}")
                .partsColorsJson("{\"handle\":\"red\"}")
                .logoConfigJson("{\"logo\":\"newlogo.png\"}")
                .textConfigJson("{\"text\":\"Hi\"}")
                .assetId(3L)
                .build();

        Asset asset = Asset.builder().id(3L).name("Cup").build();
        Design design = Design.builder()
                .id(1L)
                .name("Old Design")
                .textureMapUrl("old.png")
                .materialsJson("{}")
                .partsColorsJson("{}")
                .logoConfigJson("{}")
                .textConfigJson("{}")
                .asset(Asset.builder().id(2L).build())
                .build();

        when(designRepository.findById(1L)).thenReturn(Optional.of(design));
        when(assetRepository.findById(3L)).thenReturn(Optional.of(asset));
        when(designRepository.save(any(Design.class))).thenReturn(design);

        DesignResponseDTO response = designService.updateDesign(1L, dto);

        assertEquals("Updated Design", response.getName());
        assertEquals("updated.png", response.getTextureMapUrl());
        assertEquals(3L, response.getAssetId());
    }

    @Test
    void updateDesign_notFound() {
        DesignUpdateDTO dto = DesignUpdateDTO.builder().build();
        when(designRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> designService.updateDesign(1L, dto));
    }

    @Test
    void updateDesign_assetNotFound() {
        DesignUpdateDTO dto = DesignUpdateDTO.builder().assetId(99L).build();
        Design design = Design.builder().id(1L).build();
        when(designRepository.findById(1L)).thenReturn(Optional.of(design));
        when(assetRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> designService.updateDesign(1L, dto));
    }

    @Test
    void deleteDesign_success() {
        Design design = Design.builder().id(1L).build();
        when(designRepository.findById(1L)).thenReturn(Optional.of(design));
        doNothing().when(designRepository).delete(design);

        assertDoesNotThrow(() -> designService.deleteDesign(1L));
        verify(designRepository).delete(design);
    }

    @Test
    void deleteDesign_notFound() {
        when(designRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> designService.deleteDesign(1L));
    }

    @Test
    void getDesignByAssetId_found() {
        Design design = Design.builder().id(1L).name("AssetDesign").build();
        when(designRepository.findByAsset_Id(7L)).thenReturn(Optional.of(design));
        DesignResponseDTO dto = designService.getDesignByAssetId(7L);
        assertEquals("AssetDesign", dto.getName());
    }

    @Test
    void getDesignByAssetId_notFound() {
        when(designRepository.findByAsset_Id(8L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> designService.getDesignByAssetId(8L));
    }

    @Test
    void getDesignsByAssetIds_success() {
        Design design = Design.builder().id(1L).name("BatchDesign").build();
        Page<Design> page = new PageImpl<>(List.of(design));
        when(designRepository.findAllByAsset_IdIn(eq(List.of(1L, 2L)), any(Pageable.class))).thenReturn(page);

        Page<DesignResponseDTO> result = designService.getDesignsByAssetIds(List.of(1L, 2L), PageRequest.of(0, 10));
        assertEquals(1, result.getTotalElements());
        assertEquals("BatchDesign", result.getContent().get(0).getName());
    }

    @Test
    void getDesignsPage_success() {
        Design design = Design.builder().id(1L).name("PagedDesign").build();
        Page<Design> page = new PageImpl<>(List.of(design));
        when(designRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<DesignResponseDTO> result = designService.getDesignsPage(PageRequest.of(0, 10));
        assertEquals(1, result.getTotalElements());
        assertEquals("PagedDesign", result.getContent().get(0).getName());
    }
}