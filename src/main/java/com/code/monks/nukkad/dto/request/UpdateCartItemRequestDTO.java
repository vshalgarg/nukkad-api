package com.code.monks.nukkad.dto.request;

import lombok.Data;

@Data
public class UpdateCartItemRequestDTO {
    private Long itemId;
    private String unit;
    private Integer quantity;
}

