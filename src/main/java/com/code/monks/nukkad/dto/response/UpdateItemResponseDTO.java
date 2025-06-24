package com.code.monks.nukkad.dto.response;
import com.code.monks.nukkad.entities.CategoryEntity;
import com.code.monks.nukkad.entities.ItemEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
@Data
@AllArgsConstructor
public class UpdateItemResponseDTO {
    private Long id;
    private String name;
    private List<String> unit;
    private List<Integer> categoryIds;

    public static UpdateItemResponseDTO fromEntity(ItemEntity item) {
        return new UpdateItemResponseDTO(
                item.getId(),
                item.getName(),
                Arrays.asList(item.getUnit().getUnits()),
                item.getCategories().stream().map(CategoryEntity::getId).toList()
        );
    }
}
