package com.sideforge.dto.admin;

import com.sideforge.dto.user.UserRequestDTO;
import com.sideforge.enums.Department;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminRequestDTO extends UserRequestDTO {

    @Min(value = 1, message = "Admin level must be at least 1")
    private Integer adminLevel;

    @NotNull(message = "Department is required")
    private Department department;

    @Size(max = 255, message = "Department image URL must not exceed 255 characters")
    private String departmentImageUrl;
}
