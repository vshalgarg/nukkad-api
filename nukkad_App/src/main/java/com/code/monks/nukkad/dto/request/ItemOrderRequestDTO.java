package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.entities.ItemOrderEntity;
import lombok.Data;

@Data

public class ItemOrderRequestDTO
{
    private String name;
    private int quantity;
    private String description;


    public static ItemOrderEntity dtoToEntity(ItemOrderRequestDTO requestDTO){
        ItemOrderEntity itemOrderEntity = new ItemOrderEntity();
        itemOrderEntity.setName(requestDTO.getName());
        itemOrderEntity.setQuantity(requestDTO.getQuantity());
        itemOrderEntity.setDescription(requestDTO.getDescription());
        return itemOrderEntity;
    }
}
