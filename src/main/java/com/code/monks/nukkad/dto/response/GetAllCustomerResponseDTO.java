package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.CustomerEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllCustomerResponseDTO {

    private Long id;
    private String name;

    public static GetAllCustomerResponseDTO convertToDTO(CustomerEntity customer) {
        return new GetAllCustomerResponseDTO(
                customer.getId(),
                customer.getName()
        );
    }
}
