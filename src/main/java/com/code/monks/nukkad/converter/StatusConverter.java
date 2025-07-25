package com.code.monks.nukkad.converter;

import com.code.monks.nukkad.enums.OrderStatusEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)

public class StatusConverter implements AttributeConverter<OrderStatusEnum, Integer>
{
    @Override
    public Integer convertToDatabaseColumn(OrderStatusEnum status) {
        return status !=null? status.getCode():null;    }

    @Override
    public OrderStatusEnum convertToEntityAttribute(Integer code) {
       return code !=null? OrderStatusEnum.fromCode(code):null;
    }
}
