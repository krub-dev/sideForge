package com.sideforge.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Customization of an asset (texture, colors, materials, logos/texts).
 * ----------------------------------------------------------------
 * Attributes:
 * - id: Primary key.
 * - name: Name of the design.
 * - textureMapUrl: Custom base texture (UV).
 * - materialsJson: JSON with materials data.
 * - partsColorsJson: JSON with colors for parts.
 * - logoConfigJson: JSON with logo configuration.
 * - textConfigJson: JSON with text configuration.
 * ----------------------------------------------------------------
 * Relation:
 * - asset: Base asset being customized (1:1).
 */
@Entity
@Table(name = "designs",
        uniqueConstraints = @UniqueConstraint(columnNames = { "asset_id", "owner_id" })
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Design {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Design name is required")
    private String name;

    // Custom base texture (UV)
    private String textureMapUrl;

    // JSONs with config data (Lob for potentially large content)
    @Lob
    private String materialsJson;

    @Lob
    private String partsColorsJson;

    @Lob
    private String logoConfigJson;

    @Lob
    private String textConfigJson;

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    @NotNull(message = "Owner is required")
    private Customer owner;

    // Relation: base asset being customized (0:1)
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "asset_id", nullable = false)
    @NotNull(message = "Asset reference is required")
    private Asset asset;
}
