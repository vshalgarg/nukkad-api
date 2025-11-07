package com.code.monks.nukkad.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateCartItemRequestDTO {
    private Long itemId;
    private String unit;
    private BigDecimal quantity;
}

