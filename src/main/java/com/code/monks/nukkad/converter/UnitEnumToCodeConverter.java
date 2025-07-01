package com.code.monks.nukkad.converter;

import com.code.monks.nukkad.enums.UnitEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UnitEnumToCodeConverter implements AttributeConverter<UnitEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UnitEnum attribute) {
        return attribute == null ? null : attribute.getCode();
    }

    @Override
    public UnitEnum convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return UnitEnum.fromCode(dbData);
    }
}
