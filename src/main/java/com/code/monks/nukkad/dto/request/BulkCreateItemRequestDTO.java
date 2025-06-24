package com.code.monks.nukkad.dto.request;

import lombok.Data;

import java.util.List;
@Data
public class BulkCreateItemRequestDTO {
    private List<CreateItemRequestDTO> items;
}
