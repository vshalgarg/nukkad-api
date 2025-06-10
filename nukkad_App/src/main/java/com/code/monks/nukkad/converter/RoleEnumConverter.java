package com.code.monks.nukkad.converter;

import com.code.monks.nukkad.enums.RoleEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleEnumConverter implements AttributeConverter<RoleEnum , Integer> {

    public Integer convertToDatabaseColumn(RoleEnum attribute) {
        if (attribute == null) return null;
        return attribute.getCode();
    }

    @Override
    public RoleEnum convertToEntityAttribute(Integer dbData) {
        if (dbData == null) return null;
        return RoleEnum.fromCode(dbData);
    }

}
