package com.code.monks.nukkad.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerRequestDTO {

	@NotBlank(message = "Name is mandatory")
	private String name;

	@NotBlank(message = "Email is mandatory")
	@Email(message = "Invalid email format")
	private String email;

	@NotBlank(message = "Address is mandatory")
	private String address;

	@NotBlank(message = "Date of birth is mandatory")
	@Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "DOB must be in format YYYY-MM-DD")
	private String dob;


}
