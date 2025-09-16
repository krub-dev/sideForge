package com.sideforge.dto.asset;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String glbPath;
    private String thumbnailDefault;
    private String partsConfigJson;
}
