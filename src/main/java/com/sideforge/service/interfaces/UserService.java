package com.sideforge.service.interfaces;

import com.sideforge.dto.user.*;
import org.springframework.data.domain.*;

import java.util.List;

public interface UserService {

    // This method should not be used. User is abstract.
    // Use CustomerService or AdminService for creation!
    UserResponseDTO createUser(UserRequestDTO userRequestDTO);

    // Find a user by its unique identifier
    UserResponseDTO getUserById(Long id);

    // Retrieve all users as a list (not paginated)
    List<UserResponseDTO> getAllUsers();

    // Update a user by its unique identifier with the provided data
    UserResponseDTO updateUser(Long id, UserUpdateDTO userUpdateDTO);

    // Delete a user by its unique identifier
    void deleteUser(Long id);

    // Retrieve all users as a paginated list
    Page<UserResponseDTO> getUsersPage(Pageable pageable);

    // Retrieve users by role as a paginated list
    Page<UserResponseDTO> getUsersPageByRole(String role, Pageable pageable);
}
