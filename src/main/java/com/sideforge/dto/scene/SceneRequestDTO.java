package com.sideforge.dto.scene;

import lombok.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SceneRequestDTO {

    @NotBlank(message = "Scene name is required")
    private String name;

    private String lightingConfigJson;

    private String cameraConfigJson;

    @Size(max = 255, message = "Thumbnail URL must not exceed 255 characters")
    private String thumbnail;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @NotNull(message = "Creation date is required")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @NotNull(message = "Update date is required")
    private LocalDateTime updatedAt;

    @NotNull(message = "Owner is required")
    private Long ownerId;

    @NotNull(message = "Design is required")
    private Long designId;
}
