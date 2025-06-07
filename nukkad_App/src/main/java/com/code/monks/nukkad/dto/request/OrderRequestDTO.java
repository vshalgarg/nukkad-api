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

    private int cartId;

    private int customerId;

    private int deliveryAddressId;

//    @NotNull(message = "StoreKeeper ID is required")
    private int storeKeeperId;

    private StatusEnum statusEnum;

    public static OrderEntity toEntity(OrderRequestDTO dto) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCartId(dto.getCartId());
        orderEntity.setCustomerId(dto.getCustomerId());
        orderEntity.setDeliveryAddressId(dto.getDeliveryAddressId());
        orderEntity.setStoreKeeperId(dto.getStoreKeeperId());
        orderEntity.setStatusEnum(dto.getStatusEnum());

        return orderEntity;
    }
}
