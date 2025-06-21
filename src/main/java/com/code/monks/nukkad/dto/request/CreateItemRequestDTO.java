package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.enums.UnitEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;


@Data
public class CreateItemRequestDTO {

	@NotBlank(message = "Name cannot be blank")
	private String name;

	@NotNull(message = "Unit cannot be null")
	private UnitEnum unit;

	private List<String> imageUrls;
	private List<Long> categoryIds;


}
