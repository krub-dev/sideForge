package com.sideforge.controller;

import com.sideforge.dto.admin.*;
import com.sideforge.service.interfaces.AdminService;
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

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @WithMockUser
    @Test
    // Tests retrieving all admins returns the correct list.
    void getAllAdmins() throws Exception {
        AdminResponseDTO admin = new AdminResponseDTO();
        admin.setId(1L);
        admin.setUsername("testadmin");
        when(adminService.getAllAdmins()).thenReturn(List.of(admin));

        mockMvc.perform(get("/api/admins"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("testadmin"));
    }

    @WithMockUser
    @Test
    // Tests retrieving an admin by ID returns the correct admin.
    void getAdminById() throws Exception {
        AdminResponseDTO admin = new AdminResponseDTO();
        admin.setId(1L);
        admin.setUsername("testadmin");
        when(adminService.getAdminById(1L)).thenReturn(admin);

        mockMvc.perform(get("/api/admins/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testadmin"));
    }

    @WithMockUser
    @Test
    // Tests creating a new admin returns the created admin and correct location header.
    void createAdmin() throws Exception {
        AdminRequestDTO request = new AdminRequestDTO();
        // Check fields if necessary

        AdminResponseDTO created = new AdminResponseDTO();
        created.setId(1L);
        created.setUsername("newadmin");

        when(adminService.createAdmin(any(AdminRequestDTO.class))).thenReturn(created);

        mockMvc.perform(post("/api/admins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"newadmin\",\"email\":\"new@admin.com\",\"role\":\"ADMIN\",\"password\":\"12345678\",\"department\":\"IT\"}")
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/admins/1"))
                .andExpect(jsonPath("$.username").value("newadmin"));
    }

    @WithMockUser
    @Test
    // Tests updating an admin returns the updated admin.
    void updateAdmin() throws Exception {
        AdminResponseDTO updated = new AdminResponseDTO();
        updated.setId(1L);
        updated.setUsername("updatedadmin");

        when(adminService.updateAdmin(eq(1L), any(AdminUpdateDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/api/admins/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"updatedadmin\",\"email\":\"updated@admin.com\",\"role\":\"ADMIN\"}")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updatedadmin"));
    }

    @WithMockUser
    @Test
    // Tests deleting an admin returns no content status.
    void deleteAdmin() throws Exception {
        doNothing().when(adminService).deleteAdmin(1L);

        mockMvc.perform(delete("/api/admins/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @WithMockUser
    @Test
    // Tests retrieving a paginated list of admins returns the correct page.
    void getAdminsPage() throws Exception {
        AdminResponseDTO admin = new AdminResponseDTO();
        admin.setId(1L);
        admin.setUsername("testadmin");
        Page<AdminResponseDTO> page = new PageImpl<>(List.of(admin));
        when(adminService.getAdminsPage(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/admins/page")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].username").value("testadmin"));
    }
}
