package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.entities.CustomerEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class  CreateCustomerRequestDTO {

	@NotBlank(message = "Name is mandatory")
	private String name;

	@NotBlank(message = "Email is mandatory")
	@Email(message = "Invalid email format")
	private String email;

    @NotBlank(message = "Address Line 1 is mandatory")
	private String addressLine1;

	private String addressLine2;

	@NotBlank(message = "Landmark is mandatory")
	private String landmark;

	@NotBlank(message = "City is mandatory")
	private String city;

	@NotBlank(message = "State is mandatory")
	private String state;

	@NotBlank(message = "Pincode is mandatory")
	private String pincode;

	@NotBlank(message = "Date of birth is mandatory")
	@Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "DOB must be in format YYYY-MM-DD")
	private String dob;

	public static CustomerEntity toEntity(CreateCustomerRequestDTO dto){
		CustomerEntity customer = new CustomerEntity();
		customer.setName(dto.getName());
		customer.setEmail(dto.getEmail());
		customer.setAddressLine1(dto.getAddressLine1());
		customer.setAddressLine2(dto.getAddressLine2());
		customer.setLandmark(dto.getLandmark());
		customer.setDob(dto.getDob());
		customer.setCity(dto.getCity());
		customer.setState(dto.getState());
		customer.setPincode(dto.getPincode());
		return customer;
	}
}
