package com.sideforge.service.impl;

import com.sideforge.dto.scene.*;
import com.sideforge.exception.ResourceNotFoundException;
import com.sideforge.model.*;
import com.sideforge.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SceneServiceImplTest {

    @Mock
    private SceneRepository sceneRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private DesignRepository designRepository;

    @InjectMocks
    private SceneServiceImpl sceneService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createScene_success() {
        SceneRequestDTO dto = SceneRequestDTO.builder()
                .name("T-shirt Scene")
                .lightingConfigJson("{\"intensity\":1.0}")
                .cameraConfigJson("{\"fov\":75}")
                .thumbnail("scene.png")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .ownerId(10L)
                .designId(20L)
                .build();

        Customer owner = Customer.builder().id(10L).username("john").build();        Design design = Design.builder().id(20L).name("Modern Design").build();
        Scene scene = Scene.builder()
                .id(1L)
                .name("T-shirt Scene")
                .lightingConfigJson("{\"intensity\":1.0}")
                .cameraConfigJson("{\"fov\":75}")
                .thumbnail("scene.png")
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .owner(owner)
                .design(design)
                .build();

        when(userRepository.findById(10L)).thenReturn(Optional.of(owner));
        when(designRepository.findById(20L)).thenReturn(Optional.of(design));
        when(sceneRepository.save(any(Scene.class))).thenReturn(scene);

        SceneResponseDTO response = sceneService.createScene(dto);

        assertEquals("T-shirt Scene", response.getName());
        assertEquals("scene.png", response.getThumbnail());
        assertEquals(10L, response.getOwnerId());
        assertEquals(20L, response.getDesignId());
    }

    @Test
    void createScene_ownerNotFound() {
        SceneRequestDTO dto = SceneRequestDTO.builder().ownerId(1L).designId(2L).build();
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> sceneService.createScene(dto));
    }

    @Test
    void createScene_designNotFound() {
        SceneRequestDTO dto = SceneRequestDTO.builder().ownerId(1L).designId(2L).build();
        Admin owner = Admin.builder().id(1L).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(designRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> sceneService.createScene(dto));
    }

    @Test
    void getSceneById_found() {
        Scene scene = Scene.builder().id(1L).name("Test Scene").build();
        when(sceneRepository.findById(1L)).thenReturn(Optional.of(scene));
        SceneResponseDTO dto = sceneService.getSceneById(1L);
        assertEquals(1L, dto.getId());
        assertEquals("Test Scene", dto.getName());
    }

    @Test
    void getSceneById_notFound() {
        when(sceneRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> sceneService.getSceneById(2L));
    }

    @Test
    void updateScene_success() {
        SceneUpdateDTO dto = SceneUpdateDTO.builder()
                .name("Updated Scene")
                .lightingConfigJson("{\"intensity\":0.5}")
                .cameraConfigJson("{\"fov\":60}")
                .thumbnail("updated.png")
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now())
                .ownerId(11L)
                .designId(21L)
                .build();

        Customer newOwner = Customer.builder().id(11L).username("alice").build();
        Design newDesign = Design.builder().id(21L).name("Classic Design").build();
        Scene scene = Scene.builder()
                .id(1L)
                .name("Old Scene")
                .lightingConfigJson("{}")
                .cameraConfigJson("{}")
                .thumbnail("old.png")
                .createdAt(LocalDateTime.now().minusDays(2))
                .updatedAt(LocalDateTime.now().minusDays(2))
                .owner(Customer.builder().id(10L).build())
                .design(Design.builder().id(20L).build())
                .build();

        when(sceneRepository.findById(1L)).thenReturn(Optional.of(scene));
        when(userRepository.findById(11L)).thenReturn(Optional.of(newOwner));
        when(designRepository.findById(21L)).thenReturn(Optional.of(newDesign));
        when(sceneRepository.save(any(Scene.class))).thenReturn(scene);

        SceneResponseDTO response = sceneService.updateScene(1L, dto);

        assertEquals("Updated Scene", response.getName());
        assertEquals("updated.png", response.getThumbnail());
        assertEquals(11L, response.getOwnerId());
        assertEquals(21L, response.getDesignId());
    }

    @Test
    void updateScene_notFound() {
        SceneUpdateDTO dto = SceneUpdateDTO.builder().build();
        when(sceneRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> sceneService.updateScene(1L, dto));
    }

    @Test
    void updateScene_ownerNotFound() {
        SceneUpdateDTO dto = SceneUpdateDTO.builder().ownerId(99L).build();
        Scene scene = Scene.builder().id(1L).build();
        when(sceneRepository.findById(1L)).thenReturn(Optional.of(scene));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> sceneService.updateScene(1L, dto));
    }

    @Test
    void updateScene_designNotFound() {
        SceneUpdateDTO dto = SceneUpdateDTO.builder().designId(88L).build();
        Scene scene = Scene.builder().id(1L).build();
        when(sceneRepository.findById(1L)).thenReturn(Optional.of(scene));
        when(designRepository.findById(88L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> sceneService.updateScene(1L, dto));
    }

    @Test
    void deleteScene_success() {
        Scene scene = Scene.builder().id(1L).build();
        when(sceneRepository.findById(1L)).thenReturn(Optional.of(scene));
        doNothing().when(sceneRepository).delete(scene);
        assertDoesNotThrow(() -> sceneService.deleteScene(1L));
        verify(sceneRepository).delete(scene);
    }

    @Test
    void deleteScene_notFound() {
        when(sceneRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> sceneService.deleteScene(1L));
    }

    @Test
    void getAllScenes_success() {
        Scene scene = Scene.builder().id(1L).name("Scene1").build();
        Page<Scene> page = new PageImpl<>(List.of(scene));
        when(sceneRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<SceneResponseDTO> result = sceneService.getAllScenes(PageRequest.of(0, 10));
        assertEquals(1, result.getTotalElements());
        assertEquals("Scene1", result.getContent().get(0).getName());
    }

    @Test
    void getScenesByOwnerId_success() {
        Scene scene = Scene.builder().id(1L).name("OwnerScene").build();
        Page<Scene> page = new PageImpl<>(List.of(scene));
        when(sceneRepository.findByOwner_Id(eq(5L), any(Pageable.class))).thenReturn(page);

        Page<SceneResponseDTO> result = sceneService.getScenesByOwnerId(5L, PageRequest.of(0, 10));
        assertEquals(1, result.getTotalElements());
        assertEquals("OwnerScene", result.getContent().get(0).getName());
    }

    @Test
    void getSceneByNameAndOwner_found() {
        Scene scene = Scene.builder().id(1L).name("MyScene").build();
        when(sceneRepository.findByNameAndOwner_Id("MyScene", 2L)).thenReturn(Optional.of(scene));
        SceneResponseDTO dto = sceneService.getSceneByNameAndOwner("MyScene", 2L);
        assertNotNull(dto);
        assertEquals("MyScene", dto.getName());
    }

    @Test
    void getSceneByNameAndOwner_notFound() {
        when(sceneRepository.findByNameAndOwner_Id("NoScene", 2L)).thenReturn(Optional.empty());
        SceneResponseDTO dto = sceneService.getSceneByNameAndOwner("NoScene", 2L);
        assertNull(dto);
    }

    @Test
    void getScenesCreatedBetween_success() {
        Scene scene = Scene.builder().id(1L).name("BetweenScene").build();
        Page<Scene> page = new PageImpl<>(List.of(scene));
        LocalDateTime start = LocalDateTime.now().minusDays(5);
        LocalDateTime end = LocalDateTime.now();
        when(sceneRepository.findAllByCreatedAtBetween(eq(start), eq(end), any(Pageable.class))).thenReturn(page);

        Page<SceneResponseDTO> result = sceneService.getScenesCreatedBetween(start, end, PageRequest.of(0, 10));
        assertEquals(1, result.getTotalElements());
        assertEquals("BetweenScene", result.getContent().get(0).getName());
    }

    @Test
    void countScenesByOwner_success() {
        when(sceneRepository.countByOwner_Id(7L)).thenReturn(3L);
        long count = sceneService.countScenesByOwner(7L);
        assertEquals(3L, count);
    }
}