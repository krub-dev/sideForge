package com.sideforge.dto.design;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DesignResponseDTO {
    private Long id;
    private String name;
    private String textureMapUrl;
    private String materialsJson;
    private String partsColorsJson;
    private String logoConfigJson;
    private String textConfigJson;
    private Long assetId;
}
