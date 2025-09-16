package com.sideforge.dto.customer;

import com.sideforge.dto.user.UserResponseDTO;
import com.sideforge.enums.PreferredLanguage;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CustomerResponseDTO extends UserResponseDTO {
    private String profileImageUrl;
    private PreferredLanguage preferredLanguage;
    private Boolean isVerified;
}
