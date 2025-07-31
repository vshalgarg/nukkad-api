package com.code.monks.nukkad.converter;

import com.code.monks.nukkad.enums.NotificationStatusEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class NotificationStatusEnumConverter implements AttributeConverter<NotificationStatusEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(NotificationStatusEnum status) {
    return status !=null? status.getCode():null;    }

    @Override
    public NotificationStatusEnum convertToEntityAttribute(Integer code) {
        return code !=null? NotificationStatusEnum.fromCode(code):null;
    }

}
