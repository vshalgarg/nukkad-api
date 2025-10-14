package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.OrderEntity;
import com.code.monks.nukkad.enums.OrderStatusEnum;
import lombok.Data;

import java.util.List;

@Data
public class GetOrderHistoryByStatusResponseDTO {

    private OrderStatusEnum status;
    private List<OrderEntity> orders;
    private String message;
}
