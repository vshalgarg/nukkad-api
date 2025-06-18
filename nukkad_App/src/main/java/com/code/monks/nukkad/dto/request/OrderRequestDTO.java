package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.entities.*;
import com.code.monks.nukkad.enums.Status;
import lombok.Data;

@Data
public class OrderRequestDTO {

    private Long cart;

    private Long customer;

    private Long deliveryAddress;

//    @NotNull(message = "StoreKeeper ID is required")
    private Long storeKeeper;

    private Status status;

    public static OrderEntity toEntity(OrderRequestDTO requestDTO) {
        OrderEntity orderEntity = new OrderEntity();
        // set cart entity with only id
        CartItemEntity cart = new CartItemEntity();
        cart.setId(requestDTO.getCart());
        orderEntity.setCart(cart);
        // address entity with only id
        AddressEntity address = new AddressEntity();
        address.setId(requestDTO.getDeliveryAddress());
        orderEntity.setDeliveryAddress(address);

        orderEntity.setStatus(requestDTO.getStatus());

        // Set customer entity with only ID
        CustomerEntity customer = new CustomerEntity();
        customer.setId(requestDTO.getCustomer());
        orderEntity.setCustomer(customer);

        // Set storekeeper entity with only ID
        StorekeeperEntity storekeeper = new StorekeeperEntity();
        storekeeper.setId(requestDTO.getStoreKeeper());
        orderEntity.setStoreKeeper(storekeeper);

        return orderEntity;
    }

}
