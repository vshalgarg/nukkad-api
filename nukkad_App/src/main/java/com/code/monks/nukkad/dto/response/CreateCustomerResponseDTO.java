package com.code.monks.nukkad.dto.response;


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
}
