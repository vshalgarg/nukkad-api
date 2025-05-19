package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.CustomerEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerResponseDTO {

	private int id;

	private String name;

	private String email;

	private String address;

	private String dob;

	public static CreateCustomerResponseDTO fromDbDto(CustomerEntity entity) {
		return new CreateCustomerResponseDTO(
				entity.getId(),
				entity.getName(),
				entity.getEmail(),
				entity.getAddress(),
				entity.getDob()
		);
	}


}
