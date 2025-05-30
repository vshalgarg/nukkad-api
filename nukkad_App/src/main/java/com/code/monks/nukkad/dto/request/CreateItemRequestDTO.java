package com.code.monks.nukkad.dto.request;

import lombok.Data;

import java.util.List;


@Data
public class CreateItemRequestDTO {

	private String name;
	private String unit;
	private List<String> imageUrls;
	private List<Long> categoryIds;


}
