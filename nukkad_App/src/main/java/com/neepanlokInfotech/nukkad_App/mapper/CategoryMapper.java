package com.neepanlokInfotech.nukkad_App.mapper;

import com.neepanlokInfotech.nukkad_App.dto.GetCategoryDTO;
import com.neepanlokInfotech.nukkad_App.dto.ItemShortDTO;
import com.neepanlokInfotech.nukkad_App.entities.CategoryEntity;
import com.neepanlokInfotech.nukkad_App.entities.ItemEntity;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {

    public static GetCategoryDTO toDTO(CategoryEntity category) {
        GetCategoryDTO dto = new GetCategoryDTO();
        dto.setCategoryId(category.getCategoryId());
        dto.setCategoryName(category.getCategoryName());

        List<ItemShortDTO> itemDTOs = new ArrayList<>();
        for (ItemEntity item : category.getItems()) {
            ItemShortDTO itemDTO = new ItemShortDTO();
            itemDTO.setItemId(item.getItemId());
            itemDTO.setItemName(item.getItemName());
            itemDTO.setImage(item.getImage());
            itemDTO.setUnit(item.getUnit());
            itemDTOs.add(itemDTO);
        }

        dto.setItems(itemDTOs);
        return dto;
    }
}
