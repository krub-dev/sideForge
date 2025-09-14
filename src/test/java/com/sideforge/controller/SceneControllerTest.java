package com.sideforge.controller;

import com.sideforge.dto.scene.*;
import com.sideforge.service.interfaces.SceneService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SceneController.class)
class SceneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SceneService sceneService;

    @WithMockUser
    @Test
    // Tests creating a new scene returns the created scene and correct location header.
    void createScene() throws Exception {
        SceneRequestDTO request = SceneRequestDTO.builder()
                .name("Scene1").ownerId(1L).designId(2L)
                .lightingConfigJson("{}").cameraConfigJson("{}")
                .thumbnail("thumb.png")
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
                .build();
        SceneResponseDTO created = SceneResponseDTO.builder()
                .id(1L).name("Scene1").ownerId(1L).designId(2L).build();

        when(sceneService.createScene(any(SceneRequestDTO.class))).thenReturn(created);

        mockMvc.perform(post("/api/scenes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Scene1\",\"ownerId\":1,\"designId\":2,\"lightingConfigJson\":\"{}\",\"cameraConfigJson\":\"{}\",\"thumbnail\":\"thumb.png\",\"createdAt\":\"01-01-2024 00:00:00\",\"updatedAt\":\"01-01-2024 00:00:00\"}")
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/scenes/1"))
                .andExpect(jsonPath("$.name").value("Scene1"));
    }

    @WithMockUser
    @Test
    // Tests retrieving a scene by ID returns the correct scene.
    void getSceneById() throws Exception {
        SceneResponseDTO dto = SceneResponseDTO.builder().id(1L).name("Scene1").ownerId(1L).designId(2L).build();
        when(sceneService.getSceneById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/scenes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Scene1"));
    }

    @WithMockUser
    @Test
    // Tests updating a scene returns the updated scene.
    void updateScene() throws Exception {
        SceneResponseDTO updated = SceneResponseDTO.builder().id(1L).name("Updated").ownerId(1L).designId(2L).build();
        when(sceneService.updateScene(eq(1L), any(SceneUpdateDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/api/scenes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated\"}")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @WithMockUser
    @Test
    // Tests deleting a scene returns no content status.
    void deleteScene() throws Exception {
        doNothing().when(sceneService).deleteScene(1L);

        mockMvc.perform(delete("/api/scenes/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @WithMockUser
    @Test
    // Tests retrieving a paginated list of scenes returns the correct page.
    void getScenesPage() throws Exception {
        SceneResponseDTO dto = SceneResponseDTO.builder().id(1L).name("Scene1").ownerId(1L).designId(2L).build();
        Page<SceneResponseDTO> page = new PageImpl<>(List.of(dto));
        when(sceneService.getAllScenes(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/scenes/page")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id")
                        .param("sort", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Scene1"));
    }

    @WithMockUser
    @Test
    // Tests retrieving scenes by owner ID returns the correct page.
    void getScenesByOwnerId() throws Exception {
        SceneResponseDTO dto = SceneResponseDTO.builder().id(1L).name("Scene1").ownerId(1L).designId(2L).build();
        Page<SceneResponseDTO> page = new PageImpl<>(List.of(dto));
        when(sceneService.getScenesByOwnerId(eq(1L), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/scenes/by-owner")
                        .param("ownerId", "1")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id")
                        .param("sort", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Scene1"));
    }

    @WithMockUser
    @Test
    // Tests retrieving a scene by name and owner returns the correct scene.
    void getSceneByNameAndOwner() throws Exception {
        SceneResponseDTO dto = SceneResponseDTO.builder().id(1L).name("Scene1").ownerId(1L).designId(2L).build();
        when(sceneService.getSceneByNameAndOwner(eq("Scene1"), eq(1L))).thenReturn(dto);

        mockMvc.perform(get("/api/scenes/by-name-and-owner")
                        .param("name", "Scene1")
                        .param("ownerId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Scene1"));
    }

    @WithMockUser
    @Test
    // Tests retrieving scenes created between two dates returns the correct page.
    void getScenesCreatedBetween() throws Exception {
        SceneResponseDTO dto = SceneResponseDTO.builder().id(1L).name("Scene1").ownerId(1L).designId(2L).build();
        Page<SceneResponseDTO> page = new PageImpl<>(List.of(dto));
        when(sceneService.getScenesCreatedBetween(any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/scenes/created-between")
                        .param("start", "2024-01-01T00:00:00")
                        .param("end", "2024-12-31T23:59:59")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id")
                        .param("sort", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Scene1"));
    }

    @WithMockUser
    @Test
    // Tests counting scenes by owner returns the correct count.
    void countScenesByOwner() throws Exception {
        when(sceneService.countScenesByOwner(1L)).thenReturn(5L);

        mockMvc.perform(get("/api/scenes/count-by-owner")
                        .param("ownerId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }
}