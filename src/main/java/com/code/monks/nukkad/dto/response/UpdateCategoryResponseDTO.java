package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateCategoryResponseDTO {
    private int id;
    private String name;
    private String imageUrl;

    public static UpdateCategoryResponseDTO fromEntity(CategoryEntity entity) {
        return new UpdateCategoryResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getImage() != null ? entity.getImage().getImageUrl() : null
        );
    }
}
