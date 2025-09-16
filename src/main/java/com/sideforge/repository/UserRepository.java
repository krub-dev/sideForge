package com.sideforge.repository;

import com.sideforge.model.User;
import com.sideforge.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

public interface UserRepository extends JpaRepository<User, Long> {
    // Find user by email (for login/registration)
    Optional<User> findByEmail(String email);

    // Find user by username (for login/registration)
    Optional<User> findByUsername(String username);

    // Check if user exists by email
    boolean existsByEmail(String email);

    // Check if user exists by username
    boolean existsByUsername(String username);

    // Paginated: Find all users by role (ADMIN, CUSTOMER) with pagination and sorting
    // User lists in admin panels or user dashboards
    Page<User> findAllByRole(Role role, Pageable pageable);
}