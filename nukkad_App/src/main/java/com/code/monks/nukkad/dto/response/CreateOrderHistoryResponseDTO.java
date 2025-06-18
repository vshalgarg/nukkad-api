package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.OrderEntity;
import com.code.monks.nukkad.enums.Status;
import lombok.Data;

@Data
public class CreateOrderHistoryResponseDTO {
    private Long id;

    private Long orderId;

    private Long cartId;
    private String itemName;
    private int quantity;

    private Long customerId;
    private String customerName;

    private Long deliveryAddressId;
    private String address;

    private Long storeKeeperId;
    private String storeKeeperName;

    private Status status;

    public static CreateOrderHistoryResponseDTO fromEntity(OrderEntity order) {
        CreateOrderHistoryResponseDTO dto = new CreateOrderHistoryResponseDTO();

        dto.setId(order.getId());
        dto.setOrderId(order.getId());

        if (order.getCart() != null) {
            dto.setCartId(order.getCart().getId());
            if (order.getCart().getItem() != null) {
                dto.setItemName(order.getCart().getItem().getName());
            }
            Integer quantity = order.getCart().getQuantity();
            if (quantity != null) {
                dto.setQuantity(quantity);
            }
        }

        if (order.getCustomer() != null) {
            dto.setCustomerId(order.getCustomer().getId());
            dto.setCustomerName(order.getCustomer().getName());
        }

        if (order.getDeliveryAddress() != null) {
            dto.setDeliveryAddressId(order.getDeliveryAddress().getId());

                dto.setAddress(order.getDeliveryAddress().getAddressLine1());
            
        }

        if (order.getStoreKeeper() != null) {
            dto.setStoreKeeperId(order.getStoreKeeper().getId());
            dto.setStoreKeeperName(order.getStoreKeeper().getName());
        }

        dto.setStatus(order.getStatus());

        return dto;
    }


}
