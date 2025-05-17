package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.CategoryEntity;
import com.code.monks.nukkad.entities.ItemEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class GetAllItemResponseDTO {
    private int id;
    private String name;
    private String unit;
    private List<Integer> categoryIds;

    public static GetAllItemResponseDTO fromEntity(ItemEntity itemEntity){
        List<Integer> categoryIds = itemEntity.getCategories()
                .stream()
                .map(CategoryEntity::getId)
                .collect(Collectors.toList());
       return new GetAllItemResponseDTO(itemEntity.getId(),itemEntity.getName(),itemEntity.getUnit(),categoryIds) ;
    }
}
