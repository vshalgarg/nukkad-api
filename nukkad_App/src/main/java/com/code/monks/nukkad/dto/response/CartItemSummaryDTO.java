package com.code.monks.nukkad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemSummaryDTO {
    private Long cartItemId;
    private Long itemId;
    private String itemName;
    private int quantity;
    private String unit;

}
