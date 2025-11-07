package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.CartItemEntity;
import com.code.monks.nukkad.entities.CategoryItemImageEntity;
import com.code.monks.nukkad.entities.ItemEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Data
@AllArgsConstructor
public class GetCartItemResponseDTO {

    private Long id;
    private Long cartId;
    private Long itemId;
    private String itemName;
    private BigDecimal quantity;
    private String selectedUnit;
    private List<String> allUnits;
    private List<String> imageUrls;


    public static GetCartItemResponseDTO fromEntity(CartItemEntity entity) {
        ItemEntity item = entity.getItem();

        List<String> imageUrls = item.getImages()
                .stream()
                .map(CategoryItemImageEntity::getImageUrl)
                .collect(Collectors.toList());

        List<String> allUnits = item.getUnit() != null
                ? Arrays.stream(item.getUnit().getUnits())
                .map(String::toUpperCase)
                .collect(Collectors.toList())
                : List.of(); // return empty list if unit is null

        return new GetCartItemResponseDTO(
                entity.getId(),
                entity.getCart().getId(),
                item.getId(),
                item.getName(),
                entity.getQuantity(),
                entity.getUnit(),
                allUnits,
                imageUrls

        );
    }

}
