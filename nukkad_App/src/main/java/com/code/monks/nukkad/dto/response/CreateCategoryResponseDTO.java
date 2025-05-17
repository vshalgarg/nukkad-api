package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCategoryResponseDTO {

	private int id;

	private String name;

	public static CreateCategoryResponseDTO fromDbDto(CategoryEntity entity) {
		return new CreateCategoryResponseDTO(entity.getId(), entity.getName());
	}

}
