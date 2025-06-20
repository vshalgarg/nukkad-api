package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.AddressEntity;
import com.code.monks.nukkad.entities.CustomerEntity;
import com.code.monks.nukkad.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerResponseDTO {

	private Long id;
	private String name;
	private String email;
	private String dob;
	private RoleEnum role;

	public static CreateCustomerResponseDTO fromEntity(CustomerEntity customer) {
		CreateCustomerResponseDTO customerResponseDTO = new CreateCustomerResponseDTO();
		customerResponseDTO.setId(customer.getId());
		customerResponseDTO.setName(customer.getName());
		customerResponseDTO.setEmail(customer.getEmail());
		customerResponseDTO.setDob(customer.getDob());
		customerResponseDTO.setRole(customer.getRole());

		return customerResponseDTO;

	}
}

