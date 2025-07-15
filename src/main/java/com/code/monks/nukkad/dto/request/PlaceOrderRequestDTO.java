
package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.entities.*;
import lombok.Data;

import java.util.List;

@Data
public class PlaceOrderRequestDTO {

    private Long deliveryAddressId;
    private Long storeKeeperId;
    private List<OrderItemEntity> orderItem;
}
