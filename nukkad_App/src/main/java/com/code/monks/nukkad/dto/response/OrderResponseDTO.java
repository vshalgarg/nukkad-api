//package com.code.monks.nukkad.dto.response;
//
//import com.code.monks.nukkad.entities.CartItemEntity;
//import com.code.monks.nukkad.entities.OrderEntity;
//import com.code.monks.nukkad.enums.OrderStatusEnum;
//import lombok.Data;
//
//@Data
//public class OrderResponseDTO {
//    private Long id;
//    private Long customerId;
//    private String customerName;
//    private String customerAddress;
//    private Long storekeeperId;
//    private String storeName;
//    private OrderStatusEnum status;
//    private CartItemSummaryDTO cartItem;
//
//
//    public static OrderResponseDTO fromEntity(OrderEntity entity) {
//        OrderResponseDTO dto = new OrderResponseDTO();
//        dto.setId(entity.getId());
//        dto.setCustomerId(entity.getCustomerId());
//        dto.setStorekeeperId(entity.getStorekeeperId());
//        dto.setStatus(entity.getStatus());
//
//
//        if(entity.getCartItem() != null){
//            CartItemEntity cartItem = entity.getCartItem();
//            CartItemSummaryDTO cartItemSummaryDTO = new CartItemSummaryDTO();
//            cartItemSummaryDTO.setCartItemId(cartItem.getId());
//            cartItemSummaryDTO.setItemId((long) cartItem.getItem().getId());
//            cartItemSummaryDTO.setItemName(cartItem.getItem().getName());
//            cartItemSummaryDTO.setQuantity(cartItem.getQuantity());
//            cartItemSummaryDTO.setUnit(cartItem.getUnit());
//            dto.setCartItem(cartItemSummaryDTO);
//        }
//        return dto;
//    }
//
//}
