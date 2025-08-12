package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.*;
import com.code.monks.nukkad.enums.OrderStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class GetUserHistoryByStatusAndDateResponseDTO {
    private Long orderId;
    private LocalDateTime orderDate;
//    private String customerName;
    private AddressSnapshotDTO address;
    private Long storeKeeperId;
    private String storeName;
    private List<ItemDetailsDTO> items;
    private OrderStatusEnum orderStatus;
    private String storeKeeperNote;

    public static GetUserHistoryByStatusAndDateResponseDTO fromEntity(OrderEntity order) {
        GetUserHistoryByStatusAndDateResponseDTO dto = new GetUserHistoryByStatusAndDateResponseDTO();

        dto.setOrderId(order.getId());
        dto.setOrderDate(order.getCreatedAt());
        dto.setOrderStatus(order.getStatus());

//        // Customer Info
//        if (order.getCustomer() != null) {
//            dto.setCustomerName(order.getCustomer().getName());
//        }

        // Address Info from snapshot text field
        dto.setAddress(order.getDeliveryAddressSnapshot());

        // StoreKeeper Info
        if (order.getStoreKeeper() != null) {
            dto.setStoreKeeperId(order.getStoreKeeper().getId());
            dto.setStoreName(order.getStoreKeeper().getStoreName());
            dto.setStoreKeeperNote(order.getStoreKeeperNote());
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

                if (order.getStatus() == OrderStatusEnum.DISPATCHED || order.getStatus() == OrderStatusEnum.DELIVERED) {
                    itemDto.setPrice(orderItem.getPrice());
                } else {
                    itemDto.setPrice(null);
                }

                itemList.add(itemDto);
            }
        }

        dto.setItems(itemList);
        return dto;
    }
}
