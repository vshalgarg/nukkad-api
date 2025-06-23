package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCategoryResponseDTO {

	private int id;
	private String name;
	private String imageUrl;

	public static CreateCategoryResponseDTO fromEntity(CategoryEntity entity) {
		return new CreateCategoryResponseDTO(
				entity.getId(),
				entity.getName(),
				entity.getImage() != null ? entity.getImage().getImageUrl(): null);
	}
}
