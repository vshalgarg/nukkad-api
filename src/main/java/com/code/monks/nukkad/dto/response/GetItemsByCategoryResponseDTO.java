package com.code.monks.nukkad.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetItemsByCategoryResponseDTO {
    private String message;
    private List<GetAllItemResponseDTO> items;
}
