package com.code.monks.nukkad.converter;

import com.code.monks.nukkad.enums.Status;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)

public class StatusConverter implements AttributeConverter<Status, Integer>
{
    @Override
    public Integer convertToDatabaseColumn(Status status) {
        return status !=null? status.getCode():null;    }

    @Override
    public Status convertToEntityAttribute(Integer code) {
       return code !=null? Status.fromCode(code):null;
    }
}
