package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.enums.OrderStatusEnum;
import lombok.Data;

@Data
public class UpdateOrderStatusRequestDTO
{
    private OrderStatusEnum orderStatus;
}
