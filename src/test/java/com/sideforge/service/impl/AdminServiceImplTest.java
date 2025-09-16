package com.sideforge.service.impl;

import com.sideforge.dto.admin.*;
import com.sideforge.enums.Department;
import com.sideforge.enums.Role;
import com.sideforge.exception.ResourceNotFoundException;
import com.sideforge.model.Admin;
import com.sideforge.repository.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceImplTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAdmin_success() {
        AdminRequestDTO dto = AdminRequestDTO.builder()
                .username("adminuser")
                .email("admin@mail.com")
                .password("adminpass")
                .role(Role.ADMIN)
                .adminLevel(2)
                .department(Department.IT)
                .departmentImageUrl("dep.jpg")
                .build();

        Admin admin = Admin.builder()
                .id(1L)
                .username("adminuser")
                .email("admin@mail.com")
                .passwordHash("adminpass")
                .role(Role.ADMIN)
                .adminLevel(2)
                .department(Department.IT)
                .departmentImageUrl("dep.jpg")
                .lastLogin(LocalDateTime.now())
                .build();

        when(adminRepository.save(any(Admin.class))).thenReturn(admin);

        AdminResponseDTO response = adminService.createAdmin(dto);

        assertEquals("adminuser", response.getUsername());
        assertEquals("admin@mail.com", response.getEmail());
        assertEquals(Role.ADMIN, response.getRole());
        assertEquals(2, response.getAdminLevel());
        assertEquals(Department.IT, response.getDepartment());
        assertEquals("dep.jpg", response.getDepartmentImageUrl());
    }

    @Test
    void getAdminById_found() {
        Admin admin = Admin.builder()
                .id(1L)
                .username("admin")
                .email("admin@mail.com")
                .role(Role.ADMIN)
                .build();

        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));

        AdminResponseDTO dto = adminService.getAdminById(1L);

        assertEquals(1L, dto.getId());
        assertEquals("admin", dto.getUsername());
        assertEquals("admin@mail.com", dto.getEmail());
        assertEquals(Role.ADMIN, dto.getRole());
    }

    @Test
    void getAdminById_notFound() {
        when(adminRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> adminService.getAdminById(2L));
    }

    @Test
    void getAllAdmins_success() {
        List<Admin> admins = Arrays.asList(
                Admin.builder().id(1L).username("a").email("a@mail.com").role(Role.ADMIN).build(),
                Admin.builder().id(2L).username("b").email("b@mail.com").role(Role.ADMIN).build()
        );
        when(adminRepository.findAll()).thenReturn(admins);

        List<AdminResponseDTO> result = adminService.getAllAdmins();

        assertEquals(2, result.size());
        assertEquals("a", result.get(0).getUsername());
        assertEquals("b", result.get(1).getUsername());
    }

    @Test
    void updateAdmin_success() {
        AdminUpdateDTO dto = AdminUpdateDTO.builder()
                .username("updated")
                .email("updated@mail.com")
                .password("newpass")
                .role(Role.ADMIN)
                .adminLevel(3)
                .department(Department.HR)
                .departmentImageUrl("img2.jpg")
                .build();

        Admin admin = Admin.builder()
                .id(1L)
                .username("old")
                .email("old@mail.com")
                .passwordHash("oldpass")
                .role(Role.ADMIN)
                .adminLevel(1)
                .department(Department.IT)
                .departmentImageUrl("img.jpg")
                .build();

        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(adminRepository.save(any(Admin.class))).thenReturn(admin);

        AdminResponseDTO response = adminService.updateAdmin(1L, dto);

        assertEquals("updated", response.getUsername());
        assertEquals("updated@mail.com", response.getEmail());
        assertEquals(3, response.getAdminLevel());
        assertEquals(Department.HR, response.getDepartment());
        assertEquals("img2.jpg", response.getDepartmentImageUrl());
    }

    @Test
    void updateAdmin_notFound() {
        AdminUpdateDTO dto = AdminUpdateDTO.builder().build();
        when(adminRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> adminService.updateAdmin(1L, dto));
    }

    @Test
    void deleteAdmin_success() {
        Admin admin = Admin.builder().id(1L).build();
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));
        doNothing().when(adminRepository).delete(admin);

        assertDoesNotThrow(() -> adminService.deleteAdmin(1L));
        verify(adminRepository).delete(admin);
    }

    @Test
    void deleteAdmin_notFound() {
        when(adminRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> adminService.deleteAdmin(1L));
    }

    @Test
    void getAdminsPage_success() {
        Admin admin = Admin.builder().id(1L).username("pageadmin").email("p@mail.com").role(Role.ADMIN).build();
        Page<Admin> page = new PageImpl<>(List.of(admin));
        when(adminRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<AdminResponseDTO> result = adminService.getAdminsPage(PageRequest.of(0, 10));
        assertEquals(1, result.getTotalElements());
        assertEquals("pageadmin", result.getContent().get(0).getUsername());
    }
}