package com.code.monks.nukkad.mapper;

import com.code.monks.nukkad.dto.ItemRequestDTO;
import com.code.monks.nukkad.dto.ItemResponseDTO;
import com.code.monks.nukkad.entities.CategoryEntity;
import com.code.monks.nukkad.entities.ItemEntity;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {

	// Convert DTO to Entity
	public static ItemEntity toEntity(ItemRequestDTO dto) {
		ItemEntity item = new ItemEntity();
		item.setName(dto.getName());
		item.setImage(dto.getImage());
		item.setUnit(dto.getUnit());
		return item;
	}

	// Convert Entity to DTO
	public static ItemResponseDTO toDTO(ItemEntity item) {
		ItemResponseDTO dto = new ItemResponseDTO();
		dto.setId(item.getId());
		dto.setName(item.getName());
		dto.setImage(item.getImage());
		dto.setUnit(item.getUnit());

		List<Long> categoryIds = new ArrayList<>();
		for (CategoryEntity category : item.getCategories()) {
			categoryIds.add(category.getId());
		}

		dto.setCategoryIds(categoryIds);
		return dto;
	}

}
