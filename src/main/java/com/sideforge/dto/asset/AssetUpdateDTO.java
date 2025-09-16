package com.sideforge.dto.asset;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetUpdateDTO {
    private String name;
    private String description;
    private String glbPath;
    private String thumbnailDefault;
    private String partsConfigJson;
}
