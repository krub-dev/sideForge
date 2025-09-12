package com.sideforge.dto.customer;

import com.sideforge.dto.user.UserRequestDTO;
import com.sideforge.enums.PreferredLanguage;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRequestDTO extends UserRequestDTO {

    @Size(max = 255, message = "Profile image URL must not exceed 255 characters")
    private String profileImageUrl;

    private PreferredLanguage preferredLanguage;

    private Boolean isVerified;
}