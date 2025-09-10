package com.sideforge.model;

import com.sideforge.enums.Role;
import jakarta.persistence.*;
import lombok.*;

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
@Builder
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false, unique = true)
    protected String username;

    @Column(nullable = false, unique = true)
    protected String email;

    @Column(nullable = false)
    protected String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected Role role;

    // Relation: List of scenes owned by the user (OneToMany)
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<Scene> scenes;
}
