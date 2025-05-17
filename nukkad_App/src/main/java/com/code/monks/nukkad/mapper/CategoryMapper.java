package com.code.monks.nukkad.mapper;

import com.code.monks.nukkad.dto.ItemShortDTO;
import com.code.monks.nukkad.entities.CategoryEntity;
import com.code.monks.nukkad.entities.ItemEntity;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {

	// To map category with its items (for getCategoryById)
	public static GetCategoryRequestDTO toDTOWithItems(CategoryEntity category) {
		GetCategoryRequestDTO dto = new GetCategoryRequestDTO();
		dto.setId(category.getId());
		dto.setName(category.getName());

		List<ItemShortDTO> itemDTOs = new ArrayList<>();
		for (ItemEntity item : category.getItems()) {
			ItemShortDTO itemDTO = new ItemShortDTO();
			itemDTO.setId(item.getId());
			itemDTO.setName(item.getName());
			itemDTO.setImage(item.getImage());
			itemDTO.setUnit(item.getUnit());
			itemDTOs.add(itemDTO);
		}

		dto.setItems(itemDTOs); // Set items list
		return dto;
	}

	// To map category without its items (for getAllCategories)
	public static GetCategoryRequestDTO toDTOWithoutItems(CategoryEntity category) {
		GetCategoryRequestDTO dto = new GetCategoryRequestDTO();
		dto.setId(category.getId());
		dto.setName(category.getName());

		// No need to set items for getAllCategories
		dto.setItems(new ArrayList<>());
		return dto;
	}

}
