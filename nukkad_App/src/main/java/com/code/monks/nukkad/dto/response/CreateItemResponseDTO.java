package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.dto.request.CreateCategoryRequestDTO;
import com.code.monks.nukkad.dto.request.CreateItemRequestDTO;
import com.code.monks.nukkad.entities.CategoryEntity;
import com.code.monks.nukkad.entities.ItemEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class CreateItemResponseDTO {
    private int id;
    private String name;
    private String image;
    private String unit;
    private List<Integer> categoryIds;

    public static CreateItemResponseDTO fromEntity(ItemEntity itemEntity){
        List<Integer> categoryIds = itemEntity.getCategories()
                .stream()
                .map(CategoryEntity::getId)
                .collect(Collectors.toList());
        return new CreateItemResponseDTO(
                itemEntity.getId(),
                itemEntity.getName(),
                itemEntity.getImage(),
                itemEntity.getUnit(),
                categoryIds
        );
    }

}
