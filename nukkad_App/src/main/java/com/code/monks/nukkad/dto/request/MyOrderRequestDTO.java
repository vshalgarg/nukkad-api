package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.entities.MyOrderEntity;
import com.code.monks.nukkad.enums.OrderStatusEnum;
import lombok.Data;

@Data
public class MyOrderRequestDTO
{
    private int orderId;
    private int customerId;
    private int storeKeeperId;
    private OrderStatusEnum orderStatusEnum;

    public static MyOrderEntity toEntity(MyOrderRequestDTO requestDTO)
    {
        MyOrderEntity orderEntity = new MyOrderEntity();
        orderEntity.setOrderId(requestDTO.getOrderId());
        orderEntity.setCustomerId(requestDTO.getCustomerId());
        orderEntity.setStoreKeeperId(requestDTO.getStoreKeeperId());
        orderEntity.setOrderStatusEnum(requestDTO.getOrderStatusEnum());
        return orderEntity;
    }
}
