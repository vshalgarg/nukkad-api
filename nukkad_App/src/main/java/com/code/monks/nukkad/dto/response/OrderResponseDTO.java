package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.OrderEntity;
import com.code.monks.nukkad.enums.Status;
import lombok.Data;

@Data
public class OrderResponseDTO {

    private Long id;
    private Long cart;
    private Long customer;
    private Long deliveryAddress;
    private Long storeKeeper;
    private Status status;

    public static OrderResponseDTO toResponseDTO(OrderEntity entity) {
        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setId(entity.getId());
        responseDTO.setCart((entity.getCart().getId()));
        responseDTO.setCustomer(entity.getCustomer().getId());
        responseDTO.setDeliveryAddress(entity.getDeliveryAddress().getId());
        responseDTO.setStoreKeeper(entity.getStoreKeeper().getId());
        responseDTO.setStatus(entity.getStatus());
        return responseDTO;
    }
}
