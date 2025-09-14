package com.sideforge.controller;

import com.sideforge.dto.asset.*;
import com.sideforge.service.interfaces.AssetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(AssetController.class)
class AssetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AssetService assetService;

    @WithMockUser
    @Test
    // Tests retrieving all assets returns the correct list.
    void getAllAssets() throws Exception {
        AssetResponseDTO asset = AssetResponseDTO.builder().id(1L).name("Asset1").build();
        when(assetService.getAllAssets()).thenReturn(List.of(asset));

        mockMvc.perform(get("/api/assets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Asset1"));
    }

    @WithMockUser
    @Test
    // Tests retrieving an asset by ID returns the correct asset.
    void getAssetById() throws Exception {
        AssetResponseDTO asset = AssetResponseDTO.builder().id(1L).name("Asset1").build();
        when(assetService.getAssetById(1L)).thenReturn(asset);

        mockMvc.perform(get("/api/assets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Asset1"));
    }

    @WithMockUser
    @Test
    // Tests creating a new asset returns the created asset and correct location header.
    void createAsset() throws Exception {
        AssetRequestDTO request = AssetRequestDTO.builder()
                .name("Asset1").description("desc").glbPath("path.glb").thumbnailDefault("thumb.png").partsConfigJson("{}").build();
        AssetResponseDTO created = AssetResponseDTO.builder().id(1L).name("Asset1").build();

        when(assetService.createAsset(any(AssetRequestDTO.class))).thenReturn(created);

        mockMvc.perform(post("/api/assets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Asset1\",\"description\":\"desc\",\"glbPath\":\"path.glb\",\"thumbnailDefault\":\"thumb.png\",\"partsConfigJson\":\"{}\"}")
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/assets/1"))
                .andExpect(jsonPath("$.name").value("Asset1"));
    }

    @WithMockUser
    @Test
    // Tests updating an asset returns the updated asset.
    void updateAsset() throws Exception {
        AssetResponseDTO updated = AssetResponseDTO.builder().id(1L).name("UpdatedAsset").build();
        when(assetService.updateAsset(eq(1L), any(AssetUpdateDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/api/assets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"UpdatedAsset\",\"description\":\"desc\",\"glbPath\":\"path.glb\",\"thumbnailDefault\":\"thumb.png\",\"partsConfigJson\":\"{}\"}")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UpdatedAsset"));
    }

    @WithMockUser
    @Test
    // Tests deleting an asset returns no content status.
    void deleteAsset() throws Exception {
        doNothing().when(assetService).deleteAsset(1L);

        mockMvc.perform(delete("/api/assets/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @WithMockUser
    @Test
    // Tests retrieving a paginated list of assets returns the correct page.
    void getAssetsPage() throws Exception {
        AssetResponseDTO asset = AssetResponseDTO.builder().id(1L).name("Asset1").build();
        Page<AssetResponseDTO> page = new PageImpl<>(List.of(asset));
        when(assetService.getAssetsPage(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/assets/page")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Asset1"));
    }

    @WithMockUser
    @Test
    // Tests searching assets by name returns the correct page.
    void findAssetsByName() throws Exception {
        AssetResponseDTO asset = AssetResponseDTO.builder().id(1L).name("Asset1").build();
        Page<AssetResponseDTO> page = new PageImpl<>(List.of(asset));
        when(assetService.findAssetsByName(eq("Asset"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/assets/search")
                        .param("name", "Asset")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Asset1"));
    }
}