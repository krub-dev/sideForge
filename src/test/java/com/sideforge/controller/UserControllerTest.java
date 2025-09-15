package com.sideforge.controller;

import com.sideforge.dto.user.*;
import com.sideforge.service.interfaces.UserService;
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

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @WithMockUser
    @Test
    // Tests retrieving all users returns the correct list.
    void getAllUsers() throws Exception {
        UserResponseDTO user = new UserResponseDTO();
        user.setId(1L);
        user.setUsername("testuser");
        when(userService.getAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("testuser"));
    }

    @WithMockUser
    @Test
    // Tests retrieving a user by ID returns the correct user.
    void getUserById() throws Exception {
        UserResponseDTO user = new UserResponseDTO();
        user.setId(1L);
        user.setUsername("testuser");
        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @WithMockUser
    @Test
    // Tests updating a user returns the updated user.
    void updateUser() throws Exception {
        UserUpdateDTO updateDTO = new UserUpdateDTO();

        UserResponseDTO updatedUser = new UserResponseDTO();
        updatedUser.setId(1L);
        updatedUser.setUsername("updateduser");

        when(userService.updateUser(eq(1L), any(UserUpdateDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"updateduser\", \"role\":\"ADMIN\", \"email\":\"updated@email.com\"}")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updateduser"));
    }

    @WithMockUser
    @Test
    // Tests deleting a user returns no content status.
    void deleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @WithMockUser
    @Test
    // Tests retrieving a paginated list of users returns the correct page.
    void getUsersPage() throws Exception {
        UserResponseDTO user = new UserResponseDTO();
        user.setId(1L);
        user.setUsername("testuser");
        Page<UserResponseDTO> page = new PageImpl<>(List.of(user));
        when(userService.getUsersPage(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/users/page")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].username").value("testuser"));
    }

    @WithMockUser
    @Test
    // Tests retrieving a paginated list of users filtered by role returns the correct page.
    void getUsersPageByRole() throws Exception {
        UserResponseDTO user = new UserResponseDTO();
        user.setId(1L);
        user.setUsername("testuser");
        Page<UserResponseDTO> page = new PageImpl<>(List.of(user));
        when(userService.getUsersPageByRole(eq("ADMIN"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/users/page/role/ADMIN")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].username").value("testuser"));
    }
}
