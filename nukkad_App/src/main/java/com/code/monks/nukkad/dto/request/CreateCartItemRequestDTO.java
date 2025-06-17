package com.code.monks.nukkad.dto.request;

import lombok.Data;

@Data
public class CreateCartItemRequestDTO {

    private int itemId;
    private int quantity;
    private String unit;

}
