package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.*;
import com.code.monks.nukkad.enums.Status;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class GetUserHistoryByStatusAndDateResponseDTO {
    private Long orderId;
    private LocalDateTime orderDate;

    private String customerName;

    private Long deliveryAddressId;
    private String address;

    private Long storeKeeperId;
    private String storeName;

    private List<ItemDetailsDTO> items;

    private Status status;

    public static GetUserHistoryByStatusAndDateResponseDTO fromEntity(OrderEntity order) {
        GetUserHistoryByStatusAndDateResponseDTO dto = new GetUserHistoryByStatusAndDateResponseDTO();

        dto.setOrderId(order.getId());
        dto.setOrderDate(order.getCreatedAt());
        dto.setStatus(order.getStatus());

        // Customer Info
        if (order.getCustomer() != null) {
            dto.setCustomerName(order.getCustomer().getName());
        }

        // Address Info
        if (order.getDeliveryAddress() != null) {
            dto.setDeliveryAddressId(order.getDeliveryAddress().getId());
            dto.setAddress(order.getDeliveryAddress().getAddressLine1());
        }

        // StoreKeeper Info
        if (order.getStoreKeeper() != null) {
            dto.setStoreKeeperId(order.getStoreKeeper().getId());
            dto.setStoreName(order.getStoreKeeper().getStoreName());
        }

        // Order Items
        List<ItemDetailsDTO> itemList = new ArrayList<>();
        if (order.getOrderItems() != null) {
            for (OrderItemEntity orderItem : order.getOrderItems()) {
                if (orderItem.getItem() == null) continue;

                ItemEntity itemEntity = orderItem.getItem();
                ItemDetailsDTO itemDto = new ItemDetailsDTO();

                itemDto.setItemId(itemEntity.getId());
                itemDto.setItemName(itemEntity.getName());

                // Safely resolve unit (either enum or string)
                String rawUnit = orderItem.getUnit();
                itemDto.setUnit(rawUnit);  // Store raw unit string

                itemDto.setQuantity(orderItem.getQuantity());

                // Calculate price only for DISPATCH or DELIVERED
                double price = 0.0;
                if (order.getStatus() == Status.DISPATCH || order.getStatus() == Status.DELIVERED) {
                    String baseUnit = rawUnit != null ? rawUnit.toUpperCase() : "";
                    price = switch (baseUnit) {
                        case "KG" -> 40.0;
                        case "PKT" -> 15.0;
                        case "LTR" -> 25.0;
                        default -> 0.0;
                    };
                }
                itemDto.setPrice(price);

                itemList.add(itemDto);
            }
        }

        dto.setItems(itemList);
        return dto;
    }
}
