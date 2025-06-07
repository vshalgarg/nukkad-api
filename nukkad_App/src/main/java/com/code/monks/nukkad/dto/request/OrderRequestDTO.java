package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.entities.OrderEntity;
import com.code.monks.nukkad.enums.StatusEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

@Data
public class OrderRequestDTO {

    private Long cart;

    private Long customer;

    private Long deliveryAddress;

//    @NotNull(message = "StoreKeeper ID is required")
    private Long storeKeeper;

    private StatusEnum statusEnum;

    public static OrderEntity toEntity(OrderRequestDTO requestDTO) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.getCart().setId(requestDTO.getCart());
        orderEntity.getCustomer().setId(requestDTO.getCustomer());
        orderEntity.getDeliveryAddress().setId(requestDTO.getDeliveryAddress());
        orderEntity.getStoreKeeper().setId(requestDTO.getStoreKeeper());
        orderEntity.getStatusEnum().setId(requestDTO.getStatusEnum());

        return orderEntity;
    }
}
