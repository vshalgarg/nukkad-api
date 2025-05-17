package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetAllCategoryResponseDTO {

	private int id;

	private String name;

	public static GetAllCategoryResponseDTO fromDbDto(CategoryEntity entity) {
		return new GetAllCategoryResponseDTO(entity.getId(), entity.getName());
	}

}
