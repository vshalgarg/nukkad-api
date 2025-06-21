package com.code.monks.nukkad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponseDTO {

    private List<CreateCategoryResponseDTO> categories;
    private List<CreateItemResponseDTO> items;
}
