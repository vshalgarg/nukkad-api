package com.neepanlokInfotech.nukkad_App.mapper;

import com.neepanlokInfotech.nukkad_App.dto.ItemRequestDTO;
import com.neepanlokInfotech.nukkad_App.dto.ItemResponseDTO;
import com.neepanlokInfotech.nukkad_App.entities.CategoryEntity;
import com.neepanlokInfotech.nukkad_App.entities.ItemEntity;

import java.util.ArrayList;
import java.util.List;


public class ItemMapper {

    // Convert DTO to Entity
    public static ItemEntity toEntity(ItemRequestDTO dto) {
        ItemEntity item = new ItemEntity();
        item.setItemName(dto.getItemName());
        item.setImage(dto.getImage());
        item.setUnit(dto.getUnit());
        return item;
    }

    // Convert Entity to DTO
    public static ItemResponseDTO toDTO(ItemEntity item) {
        ItemResponseDTO dto = new ItemResponseDTO();
        dto.setItemId(item.getItemId());
        dto.setItemName(item.getItemName());
        dto.setImage(item.getImage());
        dto.setUnit(item.getUnit());


            List<Long> categoryIds = new ArrayList<>();
            for (CategoryEntity category : item.getCategories()) {
                categoryIds.add(category.getCategoryId());
            }

            dto.setCategoryIds(categoryIds);
            return dto;
        }
    }

