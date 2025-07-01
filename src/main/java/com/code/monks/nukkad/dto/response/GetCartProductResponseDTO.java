package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.CartProductEntity;
import com.code.monks.nukkad.entities.CategoryItemImageEntity;
import com.code.monks.nukkad.entities.ItemEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Data
public class GetCartProductResponseDTO {

    private Long id;
    private Long itemId;
    private String itemName;
    private int quantity;
    private String unit;
    private List<String> imageUrls;


    public static GetCartProductResponseDTO fromEntity(CartProductEntity entity) {
        ItemEntity item = entity.getItem();

        return new GetCartProductResponseDTO(
                entity.getId(),
                item.getId(),
                item.getName(),
                entity.getQuantity(),
                entity.getUnit(),
                item.getImages()
                        .stream()
                        .map(CategoryItemImageEntity::getImageUrl)
                        .collect(Collectors.toList())

        );
    }
}
