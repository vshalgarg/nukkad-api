package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.MyOrderEntity;
import lombok.Data;

@Data
public class MyOrderResponseDTO
{
    private int id;
    private int orderId;
    private int customerId;
    private int storeKeeperId;
    private String statusEnum;

    public static MyOrderResponseDTO toResponseDTO(MyOrderEntity myOrderEntity)
    {
        MyOrderResponseDTO myOrderResponseDTO = new MyOrderResponseDTO();
        myOrderResponseDTO.setId(myOrderEntity.getId());
        myOrderResponseDTO.setOrderId(myOrderEntity.getOrderId());
        myOrderResponseDTO.setCustomerId(myOrderEntity.getCustomerId());
        myOrderResponseDTO.setStoreKeeperId(myOrderResponseDTO.getStoreKeeperId());
        myOrderResponseDTO.setStatusEnum(myOrderResponseDTO.getStatusEnum());
        return myOrderResponseDTO;
    }
}
