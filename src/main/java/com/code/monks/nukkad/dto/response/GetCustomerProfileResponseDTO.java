package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.CustomerEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCustomerProfileResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String dob;
    private String mobileNumber;

    public static GetCustomerProfileResponseDTO fromEntity(CustomerEntity customer) {
        return GetCustomerProfileResponseDTO.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .dob(customer.getDob())
                .mobileNumber(customer.getMobileNumber())
                .build();
    }
}

