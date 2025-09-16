package com.sideforge.service.interfaces;

import com.sideforge.dto.admin.*;
import org.springframework.data.domain.*;

import java.util.List;

public interface AdminService {

    // Create a new admin user from an AdminRequestDTO
    AdminResponseDTO createAdmin(AdminRequestDTO adminRequestDTO);

    // Retrieve an admin user by its unique identifier
    AdminResponseDTO getAdminById(Long id);

    // Retrieve all admin users as a list (not paginated)
    List<AdminResponseDTO> getAllAdmins();

    // Update an existing admin user by its unique identifier with the provided data
    AdminResponseDTO updateAdmin(Long id, AdminUpdateDTO adminUpdateDTO);

    // Delete an admin user by its unique identifier
    void deleteAdmin(Long id);

    // Retrieve all admin users as a paginated list
    Page<AdminResponseDTO> getAdminsPage(Pageable pageable);
}
