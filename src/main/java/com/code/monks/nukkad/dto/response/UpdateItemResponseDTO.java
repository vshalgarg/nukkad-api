package com.code.monks.nukkad.dto.response;
import com.code.monks.nukkad.entities.CategoryEntity;
import com.code.monks.nukkad.entities.ItemEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
@Data
@AllArgsConstructor
public class UpdateItemResponseDTO {
    private Long id;
    private String name;
    private List<String> unit;
    private List<Long> categoryIds;

   public static UpdateItemResponseDTO fromEntity(ItemEntity item) {
       return new UpdateItemResponseDTO(
               item.getId(),
               item.getName(),
               item.getUnit() != null ? Arrays.asList(item.getUnit().getUnits()) : Collections.emptyList(),

               item.getCategories() != null
                       ? item.getCategories().stream()
                       .map(CategoryEntity::getId).toList()
                       : Collections.emptyList()
       );
   }
}
