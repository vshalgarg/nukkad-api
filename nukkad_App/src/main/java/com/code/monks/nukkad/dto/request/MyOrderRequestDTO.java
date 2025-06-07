package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.entities.MyOrderEntity;
import lombok.Data;

@Data
public class MyOrderRequestDTO
{
    private int orderId;
    private int customerId;
    private int storeKeeperId;
    private String statusEnum;

    public static MyOrderEntity toEntity(MyOrderRequestDTO requestDTO)
    {
        MyOrderEntity orderEntity = new MyOrderEntity();
        orderEntity.setOrderId(requestDTO.getOrderId());
        orderEntity.setCustomerId(requestDTO.getCustomerId());
        orderEntity.setStoreKeeperId(requestDTO.getStoreKeeperId());
        orderEntity.setStatusEnum(requestDTO.getStatusEnum());
        return orderEntity;
    }
}
