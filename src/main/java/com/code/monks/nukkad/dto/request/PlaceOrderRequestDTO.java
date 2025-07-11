
package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.entities.*;
import lombok.Data;

import java.util.List;

@Data
public class PlaceOrderRequestDTO {

    private Long deliveryAddressId;

    private Long storeKeeperId;
    private List<OrderItemEntity> orderItem;

    public static OrderEntity toEntity(PlaceOrderRequestDTO requestDTO) {
        OrderEntity orderEntity = new OrderEntity();

        // address entity with only id
        AddressEntity address = new AddressEntity();
        address.setId(requestDTO.getDeliveryAddressId());


        // Set storekeeper entity with only ID
        StorekeeperEntity storekeeper = new StorekeeperEntity();
        storekeeper.setId(requestDTO.getStoreKeeperId());

        orderEntity.setDeliveryAddress(address);
        orderEntity.setStoreKeeper(storekeeper);

        return orderEntity;
    }

}
