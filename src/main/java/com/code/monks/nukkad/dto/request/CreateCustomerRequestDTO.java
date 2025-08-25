package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.entities.CustomerEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerRequestDTO {

	@NotBlank(message = "Name is required")
	@Size(max = 50, message = "Name must be at most 50 characters")
	private String name;

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	@Size(max = 50, message = "Email must be at most 50 characters")
	private String email;

	@NotBlank(message = "Date of birth is required")
	@Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "DOB must be in format YYYY-MM-DD")
	private String dob;

	private String addressLine1;

	private String addressLine2;

	private String landmark;

	private String city;

	private String state;

	private String pincode;

	public static CustomerEntity toEntity(CreateCustomerRequestDTO dto) {
		CustomerEntity customer = new CustomerEntity();
		customer.setName(dto.getName());
		customer.setEmail(dto.getEmail());
		customer.setDob(dto.getDob());
		return customer;
	}
}
