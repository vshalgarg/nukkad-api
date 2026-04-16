package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.CategoryEntity;
import com.code.monks.nukkad.entities.CategoryItemImageEntity;
import com.code.monks.nukkad.entities.ItemEntity;
import com.code.monks.nukkad.enums.UnitEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class CreateItemResponseDTO {
    private Long id;
    private String name;
    private List<String> unit;
    private List<String> imageUrls;
    private List<Long> categoryIds;

    public static CreateItemResponseDTO fromEntity(ItemEntity itemEntity) {
        UnitEnum unitEnum = itemEntity.getUnit();

        List<Long> categoryIds = itemEntity.getCategories() != null
                ? itemEntity.getCategories().stream()
                .map(CategoryEntity::getId)
                .collect(Collectors.toList())
                : Collections.emptyList();

        List<String> imageUrls = itemEntity.getImages()
                .stream()
                .map(CategoryItemImageEntity::getImageUrl)
                .collect(Collectors.toList());

        return new CreateItemResponseDTO(
                itemEntity.getId(),
                itemEntity.getName(),
                Arrays.asList(unitEnum.getUnits()),
                imageUrls,
                categoryIds
        );
    }
}
