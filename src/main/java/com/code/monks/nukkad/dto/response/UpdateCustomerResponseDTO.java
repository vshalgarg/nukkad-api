package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.CustomerEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCustomerResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String dob;
    private String profileImageUrl;

    public static UpdateCustomerResponseDTO fromEntity(CustomerEntity customer) {
        UpdateCustomerResponseDTO dto = new UpdateCustomerResponseDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        dto.setDob(customer.getDob());
        dto.setProfileImageUrl(customer.getProfileImage());
        return dto;
    }
}
