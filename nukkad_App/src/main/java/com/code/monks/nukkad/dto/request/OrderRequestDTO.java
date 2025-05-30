//package com.code.monks.nukkad.dto.request;
//
//import com.code.monks.nukkad.entities.OrderEntity;
//import com.code.monks.nukkad.enums.StatusEnum;
//import jakarta.validation.constraints.Min;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import lombok.Data;
//
//import java.time.LocalDateTime;
//import java.time.format.TextStyle;
//import java.util.Locale;
//
//@Data
//public class OrderRequestDTO {
//
//    @NotBlank(message = "Tracking number is required")
//    private String trackingNumber;
//
//    @NotNull(message = "User Id is required")
//    private Long userId;
//
//    @Min(value = 1, message = "Order Count" +"must be at least 1")
//    private int orderCount;
//
//    @NotNull(message = "Order ID is required")
//    private Long orderId;
//
//    @NotNull(message = "Shopkeeper ID is required")
//    private Long shopKeeperId;
//
//    private StatusEnum statusEnum;
//
//    public static OrderEntity toEntity(OrderRequestDTO dto) {
//        OrderEntity orderEntity = new OrderEntity();
//        orderEntity.setTrackingNumber(dto.getTrackingNumber());
//        orderEntity.setUserId(dto.getUserId());
//        orderEntity.setOrderCount(dto.getOrderCount());
//        orderEntity.setShopKeeperId(dto.getShopKeeperId());
//        LocalDateTime now = LocalDateTime.now();
//        orderEntity.setOrderDate(now);
//        orderEntity.setStatusEnum(dto.getStatusEnum());
//        return orderEntity;
//    }
//}
