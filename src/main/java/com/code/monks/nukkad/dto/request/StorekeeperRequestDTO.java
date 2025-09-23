package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.entities.StorekeeperEntity;
import com.code.monks.nukkad.entities.StorekeeperImageEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StorekeeperRequestDTO {

	@NotBlank(message = "Name is mandatory")
	private String name;

	@NotBlank(message = "Store name is mandatory")
    private String storeName;

	@NotBlank(message = "Contact Number is Mandatory")
	@Pattern(regexp = "^[0-9]{10}$", message = "Contact Number must be a 10-digit number")
	private String contactNumber;

	@NotBlank(message = "GST_IN is mandatory")
	private String gstNum;

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

	private List<String> imageUrls;

	public static StorekeeperEntity toEntity(StorekeeperRequestDTO dto){
		StorekeeperEntity storekeeper = new  StorekeeperEntity();

		storekeeper.setName(dto.getName());
		storekeeper.setStoreName(dto.getStoreName());
		storekeeper.setContactNumber(dto.getContactNumber());
		storekeeper.setGstNum(dto.getGstNum());
		storekeeper.setAddressLine1(dto.getAddressLine1());
		storekeeper.setAddressLine2(dto.getAddressLine2());
		storekeeper.setLandmark(dto.getLandmark());
		storekeeper.setCity(dto.getCity());
		storekeeper.setState(dto.getState());
		storekeeper.setPincode(dto.getPincode());
		return storekeeper;
	}
}

