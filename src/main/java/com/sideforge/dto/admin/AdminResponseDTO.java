package com.sideforge.dto.admin;

import com.sideforge.dto.user.UserResponseDTO;
import com.sideforge.enums.Department;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AdminResponseDTO extends UserResponseDTO {
    private Integer adminLevel;
    private Department department;
    private String departmentImageUrl;
    private LocalDateTime lastLogin;
}
