package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.CartItemEntity;
import com.code.monks.nukkad.entities.OrderEntity;
import com.code.monks.nukkad.enums.Status;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CreateOrderHistoryResponseDTO {
    private Long id;

    private Long cartId;
    private List<ItemDetailsDto> items;

    private Long customerId;
    private String customerName;

    private Long deliveryAddressId;
    private String address;

    private Long storeKeeperId;
    private String storeKeeperName;
    private String contactNumber;

    private Status status;


    public static CreateOrderHistoryResponseDTO fromEntity(OrderEntity order) {
        CreateOrderHistoryResponseDTO dto = new CreateOrderHistoryResponseDTO();

        dto.setId(order.getId());

        if (order.getCustomer() != null) {
            dto.setCustomerId(order.getCustomer().getId());
            dto.setCustomerName(order.getCustomer().getName());
        }

        if (order.getCart() != null) {
            dto.setCartId(order.getCart().getId());
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
            dto.setContactNumber(order.getStoreKeeper().getMobileNumber());
        }

        dto.setStatus(order.getStatus());

        if(order.getCart() != null)
        {
            List<ItemDetailsDto> itemsDetails =new ArrayList<>();
//            for(int i =0;i<order.getCart().getItem().)
            {
                ItemDetailsDto itemDetailsDto = new ItemDetailsDto();
                itemDetailsDto.setItemId(Math.toIntExact(order.getCart().getItem().getId()));
                itemDetailsDto.setItemName(order.getCart().getItem().getName());
                itemDetailsDto.setUnit(order.getCart().getItem().getUnit());
                itemsDetails.add(itemDetailsDto);
                dto.setItems(itemsDetails);
            }
        }
        return dto;
    }


}
