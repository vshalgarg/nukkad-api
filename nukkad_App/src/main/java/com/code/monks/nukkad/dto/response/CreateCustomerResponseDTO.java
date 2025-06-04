package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.CustomerEntity;
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
	private String addressLine1;
	private String addressLine2;
	private String dob;
	private String city;
	private String state;
	private String pincode;

	public static CreateCustomerResponseDTO fromEntity(CustomerEntity customer) {
		CreateCustomerResponseDTO customerResponseDTO = new CreateCustomerResponseDTO();
		customerResponseDTO.setId(customer.getId());
		customerResponseDTO.setName(customer.getName());
		customerResponseDTO.setEmail(customer.getEmail());
		customerResponseDTO.setAddressLine1(customer.getAddressLine1());
		customerResponseDTO.setAddressLine2(customer.getAddressLine2());
		customerResponseDTO.setDob(customer.getDob());
		customerResponseDTO.setCity(customer.getCity());
		customerResponseDTO.setState(customer.getState());
		customerResponseDTO.setPincode(customer.getPincode());
		return customerResponseDTO;

	}
}

