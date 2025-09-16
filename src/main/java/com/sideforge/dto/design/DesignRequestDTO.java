package com.sideforge.dto.design;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DesignRequestDTO {

    @NotBlank(message = "Design name is required")
    private String name;

    private String textureMapUrl;

    private String materialsJson;

    private String partsColorsJson;

    private String logoConfigJson;

    private String textConfigJson;

    @NotNull(message = "Asset reference is required")
    private Long assetId;

    @NotNull(message = "Owner is required")
    private Long ownerId;
}
