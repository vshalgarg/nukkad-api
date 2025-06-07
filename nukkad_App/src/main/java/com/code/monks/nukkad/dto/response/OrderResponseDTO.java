package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.OrderEntity;
import com.code.monks.nukkad.enums.StatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderResponseDTO {

    private int id;
    private int cartId;
    private int customerId;
    private int deliveryAddressId;
    private int storeKeeperId;
    private StatusEnum statusEnum;

    public static OrderResponseDTO toResponseDTO(OrderEntity entity) {
        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setId(entity.getId());
        responseDTO.setCartId(entity.getCartId());
        responseDTO.setCustomerId(entity.getCustomerId());
        responseDTO.setDeliveryAddressId(entity.getDeliveryAddressId());
        responseDTO.setStoreKeeperId(entity.getStoreKeeperId());
        responseDTO.setStatusEnum(entity.getStatusEnum());
        return responseDTO;
    }

}
