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

	@NotBlank(message = "Address Line 1 is required")
	@Size(max = 100, message = "Address Line 1 must be at most 100 characters")
	private String addressLine1;

	@Size(max = 100, message = "Address Line 2 must be at most 100 characters")
	private String addressLine2;

	@Size(max = 20, message = "Landmark must be at most 20 characters")
	private String landmark;

	@NotBlank(message = "City is required")
	@Size(max = 30, message = "City must be at most 30 characters")
	private String city;

	@NotBlank(message = "State is required")
	@Size(max = 30, message = "State must be at most 30 characters")
	private String state;

	@NotBlank(message = "Pincode is required")
	@Pattern(regexp = "\\d{6}", message = "Pincode must be a 6-digit number")
	private String pincode;

	public static CustomerEntity toEntity(CreateCustomerRequestDTO dto) {
		CustomerEntity customer = new CustomerEntity();
		customer.setName(dto.getName());
		customer.setEmail(dto.getEmail());
		customer.setDob(dto.getDob());
		return customer;
	}
}
