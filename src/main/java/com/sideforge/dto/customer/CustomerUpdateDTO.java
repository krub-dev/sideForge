package com.sideforge.dto.customer;

import com.sideforge.dto.user.UserUpdateDTO;
import com.sideforge.enums.PreferredLanguage;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CustomerUpdateDTO extends UserUpdateDTO {

    @Size(max = 255, message = "Profile image URL must not exceed 255 characters")
    private String profileImageUrl;

    private PreferredLanguage preferredLanguage;

    private Boolean isVerified;
}
