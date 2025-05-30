//package com.code.monks.nukkad.dto.request;
//
//import com.code.monks.nukkad.entities.PlaceOrderEntity;
//import com.code.monks.nukkad.enums.PlaceOrderEnum;
//import jakarta.validation.constraints.NotNull;
//import lombok.Data;
//
//@Data
//public class PlaceOrderRequestDTO {
//    private int quantity;
//    private Long orderId;
//    private Long itemId;
//
//
////    @NotNull(message = "Order status is required")
//    private PlaceOrderEnum placeOrderEnum;
//
//    public static PlaceOrderEntity dtoToEntity(PlaceOrderRequestDTO requestDTO){
//        PlaceOrderEntity itemOrderEntity = new PlaceOrderEntity();
//        itemOrderEntity.setQuantity(requestDTO.getQuantity());
//        itemOrderEntity.setOrderId(requestDTO.getOrderId());
//        itemOrderEntity.setItemId(requestDTO.getItemId());
//        itemOrderEntity.setPlaceOrderEnum(requestDTO.getPlaceOrderEnum());
//        return itemOrderEntity;
//    }
//}
