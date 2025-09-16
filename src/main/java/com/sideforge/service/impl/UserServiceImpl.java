package com.sideforge.service.impl;

import com.sideforge.dto.user.*;
import com.sideforge.enums.Role;
import com.sideforge.exception.ResourceNotFoundException;
import com.sideforge.model.User;
import com.sideforge.repository.UserRepository;
import com.sideforge.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // User is abstract. This method must not be used.
    // Use CustomerService or AdminService to create users!
    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        throw new UnsupportedOperationException("Cannot create a generic User. Use CustomerService or AdminService for creation.");
    }

    // Find a user by its unique identifier
    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return toResponseDTO(user);
    }

    // Retrieve all users as a list (not paginated)
    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserServiceImpl::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Update a user by its unique identifier with the provided data
    @Override
    @Transactional
    public UserResponseDTO updateUser(Long id, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setUsername(userUpdateDTO.getUsername());
        user.setEmail(userUpdateDTO.getEmail());
        // Only update password if provided (empty or null = ignore)
        if (userUpdateDTO.getPassword() != null && !userUpdateDTO.getPassword().isBlank()) {
            // Password is being saved in plain text.
            // Change when Security is implemented!
            user.setPasswordHash(userUpdateDTO.getPassword());
        }
        user.setRole(userUpdateDTO.getRole());
        User saved = userRepository.save(user);
        return toResponseDTO(saved);
    }

    // Delete a user by its unique identifier
    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }

    // Retrieve all users as a paginated list
    @Override
    public Page<UserResponseDTO> getUsersPage(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserServiceImpl::toResponseDTO);
    }

    // Retrieve users by role as a paginated list
    @Override
    public Page<UserResponseDTO> getUsersPageByRole(String role, Pageable pageable) {
        return userRepository.findAllByRole(Role.valueOf(role.toUpperCase()), pageable)
                .map(UserServiceImpl::toResponseDTO);
    }

    // Helper method to convert User to UserResponseDTO
    private static UserResponseDTO toResponseDTO(User user) {
        if (user == null) {
            return null;
        }
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
