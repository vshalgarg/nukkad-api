package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.OrderEntity;
import com.code.monks.nukkad.enums.StatusEnum;
import lombok.Data;

@Data
public class OrderResponseDTO {

    private int id;
    private Long cart;
    private Long customer;
    private Long deliveryAddress;
    private Long storeKeeper;
    private StatusEnum statusEnum;

    public static OrderResponseDTO toResponseDTO(OrderEntity entity) {
        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setId(entity.getId());
        responseDTO.setCart((long) entity.getCart().getQuantity());
        responseDTO.setCustomer(entity.getCustomer().getId());
        responseDTO.setDeliveryAddress(entity.getDeliveryAddress().getId());
        responseDTO.setStoreKeeper(entity.getStoreKeeper().getId());
        responseDTO.setStatusEnum(entity.getStatusEnum());
        return responseDTO;
    }

}
