package com.sideforge.dto.scene;

import lombok.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SceneUpdateDTO {

    private String name;
    private String lightingConfigJson;
    private String cameraConfigJson;

    @Size(max = 255, message = "Thumbnail URL must not exceed 255 characters")
    private String thumbnail;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedAt;

    private Long ownerId;
    private Long designId;
}
