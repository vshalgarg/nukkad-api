package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.OrderEntity;
import com.code.monks.nukkad.enums.StatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderResponseDTO {

    private Long id;
    private Long userId;
    private String trackingNumber;
    private int orderCount;
    private LocalDateTime orderDate;
    private Long shopKeeperId;
    private StatusEnum statusOrderEnum;

    public static OrderResponseDTO toResponseDTO(OrderEntity entity) {
        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setOrderCount(entity.getOrderCount());
        responseDTO.setId(entity.getId());
        responseDTO.setUserId(entity.getUserId());
        responseDTO.setTrackingNumber(entity.getTrackingNumber());
        responseDTO.setOrderDate(entity.getOrderDate());
        responseDTO.setShopKeeperId(entity.getShopKeeperId());
        responseDTO.setStatusOrderEnum(entity.getStatusEnum());

        return responseDTO;
    }

}
