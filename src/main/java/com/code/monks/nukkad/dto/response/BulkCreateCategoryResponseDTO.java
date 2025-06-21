package com.code.monks.nukkad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class BulkCreateCategoryResponseDTO {
    private List<CreateCategoryResponseDTO> categories;
}
