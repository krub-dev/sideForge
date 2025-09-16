package com.sideforge.dto.asset;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetRequestDTO {

    @NotBlank(message = "Asset name is required")
    private String name;

    private String description;

    @NotBlank(message = "GLB path is required")
    private String glbPath;

    @Size(max = 255, message = "Thumbnail URL must not exceed 255 characters")
    private String thumbnailDefault;

    // JSON with config data
    private String partsConfigJson;
}
