package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.CartItemEntity;
import com.code.monks.nukkad.entities.ImageEntity;
import com.code.monks.nukkad.entities.ItemEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Data
public class GetCartItemResponseDto {

    private Long id;
    private int itemId;
    private String itemName;
    private int quantity;
    private String unit;
    private List<String> imageUrls;

    public static GetCartItemResponseDto fromEntity(CartItemEntity entity) {
        ItemEntity item = entity.getItem();

        return new GetCartItemResponseDto(
                entity.getId(),
                item.getId(),
                item.getName(),
                entity.getQuantity(),
                entity.getUnit(),
                item.getImages()
                        .stream()
                        .map(ImageEntity::getImageUrl)
                        .collect(Collectors.toList())
        );
    }
}
