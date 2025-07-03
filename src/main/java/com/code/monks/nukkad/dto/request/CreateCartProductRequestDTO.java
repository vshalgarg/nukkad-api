package com.code.monks.nukkad.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateCartProductRequestDTO {
    private List<CartItemRequest> items;

    @Data
    public static class CartItemRequest {
        private Long itemId;
        private int quantity;
        private String unit;
    }
}
