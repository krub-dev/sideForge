package com.sideforge.dto.admin;

import com.sideforge.dto.user.UserUpdateDTO;
import com.sideforge.enums.Department;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUpdateDTO extends UserUpdateDTO {

    @Min(value = 1, message = "Admin level must be at least 1")
    private Integer adminLevel;

    private Department department;

    @Size(max = 255, message = "Department image URL must not exceed 255 characters")
    private String departmentImageUrl;
}
