package com.sideforge.service.impl;

import com.sideforge.dto.admin.*;
import com.sideforge.enums.Role;
import com.sideforge.exception.ResourceNotFoundException;
import com.sideforge.model.Admin;
import com.sideforge.repository.AdminRepository;
import com.sideforge.service.interfaces.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Autowired
    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    // Create a new admin user from an AdminRequestDTO
    @Override
    @Transactional
    public AdminResponseDTO createAdmin(AdminRequestDTO adminRequestDTO) {
        Admin admin = Admin.builder()
                .username(adminRequestDTO.getUsername())
                .email(adminRequestDTO.getEmail())
                // WARNING: Saving plain text password. In production, always hash the password!
                .passwordHash(adminRequestDTO.getPassword())
                .role(adminRequestDTO.getRole() != null ? adminRequestDTO.getRole() : Role.ADMIN)
                .adminLevel(adminRequestDTO.getAdminLevel())
                .department(adminRequestDTO.getDepartment())
                .departmentImageUrl(adminRequestDTO.getDepartmentImageUrl())
                // lastLogin is not set here, it should be handled elsewhere
                .build();

        Admin saved = adminRepository.save(admin);
        return toResponseDTO(saved);
    }

    // Retrieve an admin user by its unique identifier
    @Override
    public AdminResponseDTO getAdminById(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + id));
        return toResponseDTO(admin);
    }

    // Retrieve all admin users as a list (not paginated)
    @Override
    public List<AdminResponseDTO> getAllAdmins() {
        return adminRepository.findAll()
                .stream()
                .map(AdminServiceImpl::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Update an existing admin user by its unique identifier with the provided data
    @Override
    @Transactional
    public AdminResponseDTO updateAdmin(Long id, AdminUpdateDTO adminUpdateDTO) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + id));
        admin.setUsername(adminUpdateDTO.getUsername());
        admin.setEmail(adminUpdateDTO.getEmail());
        // Only update password if provided (empty or null = ignore)
        if (adminUpdateDTO.getPassword() != null && !adminUpdateDTO.getPassword().isBlank()) {
            // Password is being saved in plain text.
            // Change when Security is implemented!
            admin.setPasswordHash(adminUpdateDTO.getPassword());
        }
        admin.setRole(adminUpdateDTO.getRole());
        admin.setAdminLevel(adminUpdateDTO.getAdminLevel());
        admin.setDepartment(adminUpdateDTO.getDepartment());
        admin.setDepartmentImageUrl(adminUpdateDTO.getDepartmentImageUrl());
        // lastLogin managed by login auth, not update here

        Admin saved = adminRepository.save(admin);
        return toResponseDTO(saved);
    }

    // Delete an admin user by its unique identifier
    @Override
    @Transactional
    public void deleteAdmin(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + id));
        adminRepository.delete(admin);
    }

    // Retrieve all admin users as a paginated list
    @Override
    public Page<AdminResponseDTO> getAdminsPage(Pageable pageable) {
        return adminRepository.findAll(pageable)
                .map(AdminServiceImpl::toResponseDTO);
    }

    // Helper method to map Admin to AdminResponseDTO
    private static AdminResponseDTO toResponseDTO(Admin admin) {
        if (admin == null) {
            return null;
        }
        return AdminResponseDTO.builder()
                .id(admin.getId())
                .username(admin.getUsername())
                .email(admin.getEmail())
                .role(admin.getRole())
                .adminLevel(admin.getAdminLevel())
                .department(admin.getDepartment())
                .departmentImageUrl(admin.getDepartmentImageUrl())
                .lastLogin(admin.getLastLogin())
                .build();
    }
}