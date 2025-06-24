package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.CartItemEntity;
import com.code.monks.nukkad.entities.OrderEntity;
import com.code.monks.nukkad.enums.Status;
import lombok.Data;

@Data
public class OrderResponseDTO {

    private Long id;
    private Long cartId;
    private Long customerId;
    private Long deliveryAddressId;
    private Long storeKeeperId;
    private Status status;

    public static OrderResponseDTO toResponseDTO(OrderEntity entity) {
        CartItemEntity cartItemEntity = entity.getCart();
        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setId(entity.getId());
        responseDTO.setCartId((entity.getCart().getId()));
        responseDTO.setCustomerId(entity.getCustomer().getId());
        responseDTO.setDeliveryAddressId(entity.getDeliveryAddress().getId());
        responseDTO.setStoreKeeperId(entity.getStoreKeeper().getId());
//        responseDTO.
        responseDTO.setStatus(entity.getStatus());
        return responseDTO;
    }
}
