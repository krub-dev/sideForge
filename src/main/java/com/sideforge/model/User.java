package com.sideforge.model;

import com.sideforge.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Abstract user entity.
 * ----------------------------------------------------------------
 * Attributes:
 * - id: Primary key.
 * - username: Unique username for the user.
 * - email: Unique email for the user.
 * - passwordHash: Hashed password.
 * - role: User role (CUSTOMER, ADMIN).
 * ----------------------------------------------------------------
 * Relations:
 * - scenes: List of scenes owned by the user (OneToMany).
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Username is required")
    protected String username;

    @Column(nullable = false, unique = true)
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    protected String email;

    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    protected String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Role is required")
    protected Role role;

    // Relation: List of scenes owned by the user (OneToMany)
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<Scene> scenes;
}
