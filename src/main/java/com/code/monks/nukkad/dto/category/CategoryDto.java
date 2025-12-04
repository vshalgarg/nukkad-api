package com.code.monks.nukkad.dto.category;

import com.code.monks.nukkad.entities.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@AllArgsConstructor@NoArgsConstructor
public class CategoryDto {

    private int id;
    private String name;
    private String imageUrl;

    public static CategoryDto fromEntity(CategoryEntity entity) {
        return new CategoryDto(
                entity.getId(),
                entity.getName(),
                entity.getImage() != null ? entity.getImage().getImageUrl() : null
        );
    }
}
