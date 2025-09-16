package com.sideforge.dto.scene;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SceneResponseDTO {

    private Long id;
    private String name;
    private String lightingConfigJson;
    private String cameraConfigJson;
    private String thumbnail;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedAt;

    private Long ownerId;
    private Long designId;
}
