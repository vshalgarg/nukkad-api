package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.entities.*;
import com.code.monks.nukkad.enums.Status;
import lombok.Data;

@Data
public class OrderRequestDTO {

    private Long cartId;

    private Long customerId;

    private Long deliveryAddressId;

//    @NotNull(message = "StoreKeeper ID is required")
    private Long storeKeeperId;

    private Status status;

    public static OrderEntity toEntity(OrderRequestDTO requestDTO) {
        OrderEntity orderEntity = new OrderEntity();
        // set cart entity with only id
        CartProductEntity cart = new CartProductEntity();
        cart.setId(requestDTO.getCartId());
        orderEntity.setCart(cart);
        // address entity with only id
        AddressEntity address = new AddressEntity();
        address.setId(requestDTO.getDeliveryAddressId());
        orderEntity.setDeliveryAddress(address);

        orderEntity.setStatus(requestDTO.getStatus());

        // Set customer entity with only ID
        CustomerEntity customer = new CustomerEntity();
        customer.setId(requestDTO.getCustomerId());
        orderEntity.setCustomer(customer);

        // Set storekeeper entity with only ID
        StorekeeperEntity storekeeper = new StorekeeperEntity();
        storekeeper.setId(requestDTO.getStoreKeeperId());
        orderEntity.setStoreKeeper(storekeeper);

        return orderEntity;
    }

}
