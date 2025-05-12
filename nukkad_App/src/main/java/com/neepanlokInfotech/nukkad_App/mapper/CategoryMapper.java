package com.neepanlokInfotech.nukkad_App.mapper;

import com.neepanlokInfotech.nukkad_App.dto.GetCategoryDTO;
import com.neepanlokInfotech.nukkad_App.dto.ItemShortDTO;
import com.neepanlokInfotech.nukkad_App.entities.CategoryEntity;
import com.neepanlokInfotech.nukkad_App.entities.ItemEntity;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {

    // To map category with its items (for getCategoryById)
    public static GetCategoryDTO toDTOWithItems(CategoryEntity category) {
        GetCategoryDTO dto = new GetCategoryDTO();
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

        dto.setItems(itemDTOs);  // Set items list
        return dto;
    }

    // To map category without its items (for getAllCategories)
    public static GetCategoryDTO toDTOWithoutItems(CategoryEntity category) {
        GetCategoryDTO dto = new GetCategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());

        // No need to set items for getAllCategories
        dto.setItems(new ArrayList<>());
        return dto;
    }
}

