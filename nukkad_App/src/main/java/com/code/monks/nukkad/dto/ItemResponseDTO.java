package com.code.monks.nukkad.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ItemResponseDTO {

	private Long id;

	private String name;

	private String image;

	private String unit;

	private List<Long> categoryIds;

}
