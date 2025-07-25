
package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.*;
import com.code.monks.nukkad.enums.OrderStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class GetOrderByStoreKeeperResponseDTO {
    private Long customerId;
    private String customerName;
    private String customerMobileNumber;
    private Long storeKeeperId;
    private Long deliveryAddressId;
    private String address;
    private String landmark;
    private Long orderId;
    private LocalDateTime orderDate;
    private LocalDateTime updatedAt;
    private List<ItemDetailsDTO> items;
    private OrderStatusEnum orderStatus;
    private String storeKeeperNote;

    public static GetOrderByStoreKeeperResponseDTO toEntity(OrderEntity entity ) {
        GetOrderByStoreKeeperResponseDTO responseDTO = new GetOrderByStoreKeeperResponseDTO();
        responseDTO.setOrderId(entity.getId());
        responseDTO.setOrderDate(entity.getCreatedAt());
        responseDTO.setUpdatedAt(entity.getUpdatedAt());
        
        // Customer Info
        if (entity.getCustomer() != null) {
            responseDTO.setCustomerId(entity.getCustomer().getId());
            responseDTO.setCustomerName(entity.getCustomer().getName());
            responseDTO.setCustomerMobileNumber(entity.getCustomer().getMobileNumber());
        }

        // Address Info
        if (entity.getDeliveryAddress() != null) {
            AddressEntity address = entity.getDeliveryAddress();
            responseDTO.setDeliveryAddressId(address.getId());
            responseDTO.setAddress(address.getAddressLine1());
            responseDTO.setLandmark(address.getLandmark());
        }

        // Storekeeper Info
        if (entity.getStoreKeeper() != null) {
            responseDTO.setStoreKeeperId(entity.getStoreKeeper().getId());
        }

        responseDTO.setOrderStatus(entity.getStatus());
        responseDTO.setStoreKeeperNote(entity.getStoreKeeperNote());

        // Items from OrderItemEntity
        List<ItemDetailsDTO> itemList = new ArrayList<>();
        if (entity.getOrderItems() != null) {
            for (OrderItemEntity orderItem : entity.getOrderItems()) {
                if (orderItem.getItem() == null) continue;

                ItemEntity itemEntity = orderItem.getItem();
                ItemDetailsDTO itemDTO = new ItemDetailsDTO();
                itemDTO.setItemId(itemEntity.getId());
                itemDTO.setItemName(itemEntity.getName());
                itemDTO.setUnit(orderItem.getUnit());
                itemDTO.setQuantity(orderItem.getQuantity());
                itemDTO.setPrice(orderItem.getPrice()); // direct from orderItem

                List<String> imageUrls = new ArrayList<>();  
                if (itemEntity.getImages() != null) {
                    for (CategoryItemImageEntity image : itemEntity.getImages()) {
                        if (image.getImageUrl() != null && !image.getImageUrl().isBlank()) {
                            imageUrls.add(image.getImageUrl());
                        }
                    }
                }
                itemDTO.setImageUrls(imageUrls);

                itemList.add(itemDTO);
            }
        }

        responseDTO.setItems(itemList);
        return responseDTO;
    }
}
