package com.code.monks.nukkad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BulkCreateItemResponseDTO {
    private List<CreateItemResponseDTO> items;
}
