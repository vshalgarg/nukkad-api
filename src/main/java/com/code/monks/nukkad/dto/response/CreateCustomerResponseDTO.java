package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.dto.request.CreateCustomerRequestDTO;
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
	private String mobileNumber;
	private Long addressId;

	public static CreateCustomerResponseDTO fromEntity(CustomerEntity customer) {
		CreateCustomerResponseDTO customerResponseDTO = new CreateCustomerResponseDTO();
		customerResponseDTO.setId(customer.getId());
		customerResponseDTO.setName(customer.getName());
		customerResponseDTO.setEmail(customer.getEmail());
		customerResponseDTO.setDob(customer.getDob());
		customerResponseDTO.setMobileNumber(customer.getMobileNumber());
		return customerResponseDTO;
	}

	public static AddressEntity setAddress(CreateCustomerRequestDTO dto, Long id,String name, String mobileNumber){
		AddressEntity address = new AddressEntity();
		address.setAddressLine1(dto.getAddressLine1());
		address.setAddressLine2(dto.getAddressLine2());
		address.setLandmark(dto.getLandmark());
		address.setCity(dto.getCity());
		address.setState(dto.getState());
		address.setPincode(dto.getPincode());
		address.setCustomerId(id);
		address.setIsDefault(true);
		address.setName(name);
		address.setMobileNumber(mobileNumber);
		return address;
	}
}

