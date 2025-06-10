package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.MyOrderEntity;
import com.code.monks.nukkad.enums.OrderStatusEnum;
import lombok.Data;

@Data
public class MyOrderResponseDTO
{
    private int id;
    private int orderId;
    private int customerId;
    private int storeKeeperId;
    private OrderStatusEnum orderStatusEnum;

    public static MyOrderResponseDTO toResponseDTO(MyOrderEntity myOrderEntity)
    {
        MyOrderResponseDTO myOrderResponseDTO = new MyOrderResponseDTO();
        myOrderResponseDTO.setId(myOrderEntity.getId());
        myOrderResponseDTO.setOrderId(myOrderEntity.getOrderId());
        myOrderResponseDTO.setCustomerId(myOrderEntity.getCustomerId());
        myOrderResponseDTO.setStoreKeeperId(myOrderEntity.getStoreKeeperId());
        myOrderResponseDTO.setOrderStatusEnum(myOrderEntity.getOrderStatusEnum());
        return myOrderResponseDTO;
    }

}
