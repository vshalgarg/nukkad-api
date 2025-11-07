package com.code.monks.nukkad.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateCartItemRequestDTO {
    private List<CartItemRequest> items;

    @Data
    public static class CartItemRequest {
        private Long itemId;
        private BigDecimal quantity;
        private String unit;
    }
}
