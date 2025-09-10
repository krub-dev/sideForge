package com.sideforge.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Represents the 3D scene (camera, lights, owner, design, etc.).
 * ----------------------------------------------------------------
 * Attributes:
 * - id: Primary key.
 * - name: Scene name.
 * - lightingConfigJson: JSON with lighting configuration.
 * - cameraConfigJson: JSON with camera configuration.
 * - thumbnail: URL or path to a preview image.
 * - createdAt: When the scene was created.
 * - updatedAt: When the scene was last updated.
 * - owner: Scene's owner user.
 * ----------------------------------------------------------------
 * Relations:
 * - owner: User who owns the scene (ManyToOne).
 * - design: Design shown in the scene, associated and customized (OneToOne).
 */
@Entity
@Table(name = "scenes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Scene {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Lob
    private String lightingConfigJson;

    @Lob
    private String cameraConfigJson;

    private String thumbnail;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Relation: Scene's owner user (ManyToOne)
    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    // Relation: Design shown in the scene, associated and customized (OneToOne)
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "design_id", referencedColumnName = "id", nullable = false, unique = true)
    private Design design;
}
