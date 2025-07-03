package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.CategoryEntity;
import com.code.monks.nukkad.entities.CategoryItemImageEntity;
import com.code.monks.nukkad.entities.ItemEntity;
import com.code.monks.nukkad.enums.UnitEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class GetAllItemResponseDTO {

    private Long id;
    private String name;
   private List<String> unit;
    private List<String> imageUrls;
    private List<Integer> categoryIds;

    public static GetAllItemResponseDTO fromEntity(ItemEntity itemEntity) {
        UnitEnum unitEnum = itemEntity.getUnit();


        List<Integer> categoryIds = itemEntity.getCategories()
                .stream()
                .map(CategoryEntity::getId)
                .collect(Collectors.toList());

        List<String> imageUrls = itemEntity.getImages()
                .stream()
                .map(CategoryItemImageEntity::getImageUrl)
                .collect(Collectors.toList());

        return new GetAllItemResponseDTO(
                itemEntity.getId(),
                itemEntity.getName(),
                Arrays.asList(unitEnum.getUnits()),
                imageUrls,
                categoryIds
        );
    }
}
