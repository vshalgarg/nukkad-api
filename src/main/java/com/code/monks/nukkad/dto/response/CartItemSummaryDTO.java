package com.code.monks.nukkad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemSummaryDTO {
    private Long cartItemId;
    private Long itemId;
    private String itemName;
    private BigDecimal quantity;
    private String unit;

}
