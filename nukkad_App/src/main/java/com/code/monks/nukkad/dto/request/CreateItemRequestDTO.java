package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.enums.UnitEnum;
import lombok.Data;

import java.util.List;


@Data
public class CreateItemRequestDTO {

	private String name;
	private String unit;
	private int quantity;
	private List<String> imageUrls;
	private List<Long> categoryIds;


}
