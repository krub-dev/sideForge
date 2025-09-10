package com.sideforge.model;

import jakarta.persistence.*;
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
@Table(name = "designs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Design {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // Custom base texture (UV)
    private String textureMapUrl;

    // JSONs with config data
    @Lob
    private String materialsJson;

    @Lob
    private String partsColorsJson;

    @Lob
    private String logoConfigJson;

    @Lob
    private String textConfigJson;

    // Relation: base asset being customized (1:1)
    @OneToOne
    @JoinColumn(name = "asset_id", nullable = false, unique = true)
    private Asset asset;
}
