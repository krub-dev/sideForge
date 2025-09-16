package com.sideforge.dto.user;

import com.sideforge.enums.Role;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private Role role;
}
