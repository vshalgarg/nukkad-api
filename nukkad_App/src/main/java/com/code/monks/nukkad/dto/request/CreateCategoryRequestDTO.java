package com.code.monks.nukkad.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCategoryRequestDTO {

	@NotBlank(message = "Category is mandatory")
	private String name;

	@NotBlank(message = "image url is mandatory")
	private String imageUrl;

}
