package com.code.monks.nukkad.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateCartItemRequestDTO {
    private List<CartItemRequest> items;

    @Data
    public static class CartItemRequest {
        private int itemId;
        private int quantity;
        private String unit;
    }
}
