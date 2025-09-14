package com.sideforge.controller;

import com.sideforge.dto.design.*;
import com.sideforge.service.interfaces.DesignService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DesignController.class)
class DesignControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DesignService designService;

    @WithMockUser
    @Test
    // Tests creating a new design returns the created design and correct location header.
    void createDesign() throws Exception {
        DesignRequestDTO request = DesignRequestDTO.builder()
                .name("Design1").assetId(1L).build();
        DesignResponseDTO created = DesignResponseDTO.builder()
                .id(1L).name("Design1").assetId(1L).build();

        when(designService.createDesign(any(DesignRequestDTO.class))).thenReturn(created);

        mockMvc.perform(post("/api/designs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Design1\",\"assetId\":1}")
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/designs/1"))
                .andExpect(jsonPath("$.name").value("Design1"));
    }

    @WithMockUser
    @Test
    // Tests retrieving a design by ID returns the correct design.
    void getDesignById() throws Exception {
        DesignResponseDTO dto = DesignResponseDTO.builder().id(1L).name("Design1").assetId(1L).build();
        when(designService.getDesignById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/designs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Design1"));
    }

    @WithMockUser
    @Test
    // Tests retrieving all designs returns the correct list.
    void getAllDesigns() throws Exception {
        DesignResponseDTO dto = DesignResponseDTO.builder().id(1L).name("Design1").assetId(1L).build();
        when(designService.getAllDesigns()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/designs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Design1"));
    }

    @WithMockUser
    @Test
    // Tests updating a design returns the updated design.
    void updateDesign() throws Exception {
        DesignResponseDTO updated = DesignResponseDTO.builder().id(1L).name("Updated").assetId(1L).build();
        when(designService.updateDesign(eq(1L), any(DesignUpdateDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/api/designs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated\"}")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @WithMockUser
    @Test
    // Tests deleting a design returns no content status.
    void deleteDesign() throws Exception {
        doNothing().when(designService).deleteDesign(1L);

        mockMvc.perform(delete("/api/designs/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @WithMockUser
    @Test
    // Tests retrieving a design by asset ID returns the correct design.
    void getDesignByAssetId() throws Exception {
        DesignResponseDTO dto = DesignResponseDTO.builder().id(1L).name("Design1").assetId(1L).build();
        when(designService.getDesignByAssetId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/designs/by-asset/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Design1"));
    }

    @WithMockUser
    @Test
    // Tests retrieving a paginated list of designs returns the correct page.
    void getDesignsPage() throws Exception {
        DesignResponseDTO dto = DesignResponseDTO.builder().id(1L).name("Design1").assetId(1L).build();
        Page<DesignResponseDTO> page = new PageImpl<>(List.of(dto));
        when(designService.getDesignsPage(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/designs/page")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id")
                        .param("sort", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Design1"));
    }

    @WithMockUser
    @Test
    // Tests retrieving designs by a list of asset IDs returns the correct page.
    void getDesignsByAssetIds() throws Exception {
        DesignResponseDTO dto = DesignResponseDTO.builder().id(1L).name("Design1").assetId(1L).build();
        Page<DesignResponseDTO> page = new PageImpl<>(List.of(dto));
        when(designService.getDesignsByAssetIds(anyList(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/designs/by-assets")
                        .param("assetIds", "1")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id")
                        .param("sort", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Design1"));
    }
}