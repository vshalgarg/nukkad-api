package com.code.monks.nukkad.converter;

import com.code.monks.nukkad.enums.AddressLabelEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AddressLabelEnumConverter implements AttributeConverter<AddressLabelEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(AddressLabelEnum attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public AddressLabelEnum convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return AddressLabelEnum.fromCode(dbData);
    }
}
