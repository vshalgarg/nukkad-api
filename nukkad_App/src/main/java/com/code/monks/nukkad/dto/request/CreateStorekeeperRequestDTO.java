package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.entities.StorekeeperEntity;
import com.code.monks.nukkad.enums.RoleEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateStorekeeperRequestDTO {

	@NotBlank(message = "Name is mandatory")
	private String name;

	@NotBlank(message = "Store name is mandatory")
    private String storeName;

	@NotBlank(message = "Store number is mandatory")
	private String storeNumber;

	@NotBlank(message = " Contact number is mandatory")
	private String contactNumber;

	@NotBlank(message = "GST_IN is mandatory")
	private String gstIn;

	@NotBlank(message = "Address Line 1 is mandatory")
	private String addressLine1;

	@NotBlank(message = "Address Line 2 is mandatory")
	private String addressLine2;

	@NotBlank(message = "City is mandatory")
	private String city;

	@NotBlank(message = "State is mandatory")
	private String state;

	@NotBlank(message = "Pincode is mandatory")
    private String pincode;


	public static StorekeeperEntity toEntity(CreateStorekeeperRequestDTO dto){
		StorekeeperEntity storekeeper = new  StorekeeperEntity();

		storekeeper.setName(dto.getName());
		storekeeper.setStoreName(dto.getStoreName());
		storekeeper.setStoreNumber(dto.getStoreNumber());
		storekeeper.setContactNumber(dto.getContactNumber());
		storekeeper.setGstIn(dto.getGstIn());
		storekeeper.setAddressLine1(dto.getAddressLine1());
		storekeeper.setAddressLine2(dto.getAddressLine2());
		storekeeper.setCity(dto.getCity());
		storekeeper.setState(dto.getState());
		storekeeper.setPincode(dto.getPincode());
		storekeeper.setRole(RoleEnum.STOREKEEPER);
		return storekeeper;

	}
}

