package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.AddressEntity;
import com.code.monks.nukkad.entities.CustomerEntity;
import com.code.monks.nukkad.entities.OrderEntity;
import com.code.monks.nukkad.enums.Status;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CreateCustomerOrderResponseDTO
{
    private Long id;

    private Long cart;
    private List<ItemDetailsDto> items;


    private Long customer;
    private String customerName;
//    private String phoneNumber;

    private Long deliveryAddress;
    private String address;

    private Long storeKeeper;

    private Status status;

    public static CreateCustomerOrderResponseDTO toEntity(OrderEntity entity)
    {
        CreateCustomerOrderResponseDTO responseDTO = new CreateCustomerOrderResponseDTO();

        responseDTO.setId(entity.getId());
        responseDTO.setCart(entity.getCart() != null ? entity.getCart().getId() : null);

        if (entity.getCustomer() != null) {
            CustomerEntity customer = entity.getCustomer();
            responseDTO.setCustomer(customer.getId());
            responseDTO.setCustomerName(customer.getName());
//            responseDTO.setPhoneNumber(customer.getPhoneNumber());
        }

        if (entity.getDeliveryAddress() != null) {
            AddressEntity address = entity.getDeliveryAddress();
            responseDTO.setDeliveryAddress(address.getId());
            responseDTO.setAddress(address.getAddressLine1());
        }

        if (entity.getStoreKeeper() != null) {
            responseDTO.setStoreKeeper(entity.getStoreKeeper().getId());
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
