package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.entities.CustomerEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCustomerRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Date of birth is required")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "DOB must be in format YYYY-MM-DD")
    private String dob;

    private String profileImageUrl;

    public static CustomerEntity updateEntity(CustomerEntity customer, UpdateCustomerRequestDTO dto) {
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setDob(dto.getDob());
        if (dto.getProfileImageUrl() != null && !dto.profileImageUrl.isBlank()) {
            customer.setProfileImage(dto.getProfileImageUrl());
        }
        return customer;
    }

}
