package com.code.monks.nukkad.converter;

import com.code.monks.nukkad.enums.StatusEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)

public class StatusEnumConverter implements AttributeConverter<StatusEnum, Integer>
{
    @Override
    public Integer convertToDatabaseColumn(StatusEnum statusEnum) {
        return statusEnum !=null? statusEnum.getCode():null;    }

    @Override
    public StatusEnum convertToEntityAttribute(Integer code) {
       return code !=null? StatusEnum.fromCode(code):null;
    }
}
