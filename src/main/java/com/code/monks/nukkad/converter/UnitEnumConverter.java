package com.code.monks.nukkad.converter;

import com.code.monks.nukkad.enums.UnitEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UnitEnumConverter implements AttributeConverter<UnitEnum, String> {

    @Override
    public String convertToDatabaseColumn(UnitEnum attribute) {

        return attribute == null ? null : attribute.name();
    }

    @Override
    public UnitEnum convertToEntityAttribute(String dbData) {
        if (dbData == null)  {
            return null;
        }

        try {
            return UnitEnum.valueOf(dbData);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UnitEnum name: " + dbData);
        }
    }
}
