package com.sideforge.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

/**
 * Represents the base 3D asset (t-shirt, mug, etc.).
 * ----------------------------------------------------------------
 * Attributes:
 * - id: Primary key.
 * - name: Name of the asset.
 * - description: Description of the asset.
 * - glbPath: Path to the GLB 3D model file.
 * - thumbnailDefault: Path or URL for the default thumbnail image.
 * - partsConfigJson: JSON with customizable parts definition.
 * ----------------------------------------------------------------
 * Relation:
 * - design: Unique base design associated with the asset (OneToOne, Design).
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Asset name is required")
    private String name;

    private String description;

    @NotBlank(message = "GLB path is required")
    private String glbPath;

    private String thumbnailDefault;

    // JSON with config data (Lob for potentially large content)
    @Lob
    private String partsConfigJson;

    // Relation: Design associated with the asset (OneToMany)
    @OneToMany(mappedBy = "asset")
    private List<Design> designs;
}
