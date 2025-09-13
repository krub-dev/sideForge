package com.sideforge.model;

import com.sideforge.enums.Department;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Admin entity representing a system administrator.
 * Inherits from User.
 * ----------------------------------------------------------------
 * Attributes:
 * - adminLevel: Level or rank of the admin.
 * - department: Department to which the admin belongs.
 * - departmentImageUrl: URL to the department image.
 * - lastLogin: Timestamp of the admin's last login.
 */
@Entity
@Table(name = "admins")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Admin extends User {
    @Column
    private Integer adminLevel;

    @Enumerated(EnumType.STRING)
    private Department department;

    private String departmentImageUrl;

    private LocalDateTime lastLogin;
}
