package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.ItemOrderEntity;
import lombok.Data;

@Data
public class ItemOrderResponseDTO
{
    private String name;
    private String description;
    private Long id;
    private String status;
    private int quantity;

    public static ItemOrderResponseDTO fromDbToDto(ItemOrderEntity itemOrderEntity){
        ItemOrderResponseDTO responseDTO = new ItemOrderResponseDTO();
        responseDTO.setName(itemOrderEntity.getName());
        responseDTO.setDescription(itemOrderEntity.getDescription());
        responseDTO.setQuantity(itemOrderEntity.getQuantity());
        return responseDTO;
    }
}
