package com.sideforge.dto.customer;

import com.sideforge.dto.user.UserResponseDTO;
import com.sideforge.enums.PreferredLanguage;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponseDTO extends UserResponseDTO {
    private String profileImageUrl;
    private PreferredLanguage preferredLanguage;
    private Boolean isVerified;
}
