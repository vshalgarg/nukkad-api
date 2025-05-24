package com.code.monks.nukkad.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateShopkeeperRequestDTO {

	@NotBlank(message = "Name is mandatory")
	private String name;

	@NotBlank(message = "Store number is mandatory")
	private String storeNumber;

	@NotBlank(message = "GSTIN is mandatory")
	private String gstIn;

	@NotBlank(message = "Address is mandatory")
	private String address;

	@NotBlank(message = "City is mandatory")
	private String city;

	private List<String> pictures;
}

