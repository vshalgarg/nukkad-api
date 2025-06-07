//package com.code.monks.nukkad.dto.response;
//
//import com.code.monks.nukkad.entities.PlaceOrderEntity;
//import com.code.monks.nukkad.enums.PlaceOrderEnum;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class PlaceOrderResponseDTO {
//    private int id;
//    private int itemId;
//    private int orderId;
//    private PlaceOrderEnum status;
//    private int quantity;
//
//    public static PlaceOrderResponseDTO mapToResponse(PlaceOrderEntity entity) {
//        PlaceOrderResponseDTO dto = new PlaceOrderResponseDTO();
//        dto.setId(entity.getId());
//        dto.setItemId(entity.getItem().getId());
//        dto.setOrderId(entity.getOrder().getId());
//        dto.setStatus(entity.getStatus());
//        dto.setQuantity(entity.getQuantity());
//        return dto;
//    }
//}
//
