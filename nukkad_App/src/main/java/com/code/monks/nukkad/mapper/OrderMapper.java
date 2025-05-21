package com.code.monks.nukkad.mapper;

import com.code.monks.nukkad.dto.request.OrderRequestDTO;
import com.code.monks.nukkad.dto.response.OrderResponseDTO;
import com.code.monks.nukkad.entities.OrderEntity;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

public class OrderMapper {

    // Convert Request DTO to Entity

    public static OrderEntity toEntity(OrderRequestDTO dto) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(dto.getOrderId());
        orderEntity.setStatus(dto.getStatus());
        orderEntity.setTrackingNumber(dto.getTrackingNumber());
        orderEntity.setQuantity(dto.getQuantity());
        orderEntity.setShopKeeperId(dto.getShopKeeperId());
//        orderEntity.setStatusOrderEnum(dto.getStatusOrderEnum());

        LocalDateTime now = LocalDateTime.now();
        orderEntity.setOrderDate(now);
        orderEntity.setDayName(now.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        return orderEntity;
    }

    // Convert Entity to ResponseDTO

    public static OrderResponseDTO toResponseDTO(OrderEntity entity) {
        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setOrderId(entity.getOrderId());
        responseDTO.setStatus(entity.getStatus());
        responseDTO.setQuantity(entity.getQuantity());
        responseDTO.setId(entity.getId());
        responseDTO.setOrderDate(entity.getOrderDate());
        responseDTO.setTrackingNumber(entity.getTrackingNumber());
        responseDTO.setDayName(entity.getDayName());
        responseDTO.setShopKeeperId(entity.getShopKeeperId());
        responseDTO.setStatusOrderEnum(entity.getStatusOrderEnum());

        return responseDTO;
    }
}