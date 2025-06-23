package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.OrderEntity;
import com.code.monks.nukkad.enums.Status;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CreateStoreKeeperOrderResponseDTO {
    private Long id;

    private Long cart;
    private List<ItemDetailsDto> items;

    private Long storeKeeperId;
    private String storeName;
    private String contactNumber;

    private Long deliveryAddressId;
    private String address;

    private Status status;

    public static CreateStoreKeeperOrderResponseDTO toEntity(OrderEntity entity) {
        CreateStoreKeeperOrderResponseDTO responseDTO = new CreateStoreKeeperOrderResponseDTO();

        responseDTO.setId(entity.getId());
        responseDTO.setCart(entity.getCart() != null ? entity.getCart().getId() : null);

        if (entity.getStoreKeeper() != null) {
            responseDTO.setStoreKeeperId(entity.getStoreKeeper().getId());
            responseDTO.setStoreName(entity.getStoreKeeper().getName());
            responseDTO.setContactNumber(entity.getStoreKeeper().getContactNumber());
        }

        if (entity.getDeliveryAddress() != null) {
            responseDTO.setDeliveryAddressId(entity.getDeliveryAddress().getId());
            responseDTO.setAddress(entity.getDeliveryAddress().getAddressLine1());
        }

        responseDTO.setStatus(entity.getStatus());

        if(entity.getCart() != null)
        {
            List<ItemDetailsDto> itemsDetails =new ArrayList<>();
//            for(int i =0;i<order.getCart().getItem().)
            {
                ItemDetailsDto itemDetailsDto = new ItemDetailsDto();
                itemDetailsDto.setItemId(Math.toIntExact(entity.getCart().getItem().getId()));
                itemDetailsDto.setItemName(entity.getCart().getItem().getName());
                itemDetailsDto.setUnit(entity.getCart().getItem().getUnit());
                itemsDetails.add(itemDetailsDto);
                responseDTO.setItems(itemsDetails);
            }
        }

        return responseDTO;
    }
}

