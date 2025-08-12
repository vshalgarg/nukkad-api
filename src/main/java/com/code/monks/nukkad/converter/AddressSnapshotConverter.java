package com.code.monks.nukkad.converter;

import com.code.monks.nukkad.dto.response.AddressSnapshotDTO;
import com.code.monks.nukkad.exception.UnhandledException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import static com.code.monks.nukkad.enums.ResponseErrorCodes.ERROR_TO_CONVERT_JAVA_OBJECT_TO_STRING;
import static com.code.monks.nukkad.enums.ResponseErrorCodes.ERROR_TO_CONVERT_STRING_TO_JAVA_OBJECT;

@Converter(autoApply = true)
public class AddressSnapshotConverter implements AttributeConverter<AddressSnapshotDTO, String> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(AddressSnapshotDTO attribute) {
        try {
            if (attribute == null) return null;
            return mapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new UnhandledException(ERROR_TO_CONVERT_JAVA_OBJECT_TO_STRING, e);
        }
    }

    @Override
    public AddressSnapshotDTO convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isEmpty()) return null;
            return mapper.readValue(dbData, AddressSnapshotDTO.class);
        } catch (Exception e) {
            throw new UnhandledException(ERROR_TO_CONVERT_STRING_TO_JAVA_OBJECT, e);
        }
    }
}
