package com.sideforge.model;

import com.sideforge.enums.PreferredLanguage;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Customer entity representing an end user of the system.
 * Inherits from User.
 * ----------------------------------------------------------------
 * Attributes:
 * - profileImageUrl: URL to the profile image of the customer.
 * - preferredLanguage: Customer's preferred language.
 * - isVerified: Indicates if the customer account is verified.
 */
@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Customer extends User {
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private PreferredLanguage preferredLanguage;

    private Boolean isVerified;
}
