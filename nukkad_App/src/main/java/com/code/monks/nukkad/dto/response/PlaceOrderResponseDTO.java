package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.PlaceOrderEntity;
import com.code.monks.nukkad.enums.PlaceOrderEnum;
import lombok.Data;

@Data
public class PlaceOrderResponseDTO
{
    private Long id;
    private Long orderId;
    private Long itemId;
    private int quantity;
    private PlaceOrderEnum placeOrderEnum;


    public static PlaceOrderResponseDTO fromDbToDto(PlaceOrderEntity placeOrderEntity){
        PlaceOrderResponseDTO responseDTO = new PlaceOrderResponseDTO();
        responseDTO.setId(placeOrderEntity.getId());
        responseDTO.setOrderId(placeOrderEntity.getOrderId());
        responseDTO.setItemId(placeOrderEntity.getItemId());
        responseDTO.setQuantity(placeOrderEntity.getQuantity());
        responseDTO.setPlaceOrderEnum(placeOrderEntity.getPlaceOrderEnum());
        return responseDTO;
    }
}
