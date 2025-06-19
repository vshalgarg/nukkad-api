package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.AddressEntity;
import lombok.Data;

@Data
public class AddressResponseDTO {
    private Long id;
    private String addressLine1;
    private String addressLine2;
    private String landmark;
    private String city;
    private String state;
    private String pincode;


    public static AddressResponseDTO fromEntity(AddressEntity entity) {
        AddressResponseDTO dto = new AddressResponseDTO();

        dto.setId(entity.getId());
        dto.setAddressLine1(entity.getAddressLine1());
        dto.setAddressLine2(entity.getAddressLine2());
        dto.setLandmark(entity.getLandmark());
        dto.setCity(entity.getCity());
        dto.setState(entity.getState());
        dto.setPincode(entity.getPincode());

        return dto;
    }

}
