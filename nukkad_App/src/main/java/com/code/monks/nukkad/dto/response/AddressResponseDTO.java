package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.AddressEntity;
import com.code.monks.nukkad.enums.AddressLabel;
import lombok.Data;

@Data
public class AddressResponseDTO {
    private int id;
    private AddressLabel label;
    private String addressLine1;
    private String addressLine2;
    private String landmark;
    private String city;
    private String state;
    private String pincode;
    private boolean isSelected;

    public static AddressResponseDTO fromEntity(AddressEntity entity) {
        AddressResponseDTO addressResponseDTO = new AddressResponseDTO();
        addressResponseDTO.setId(entity.getId());
        addressResponseDTO.setLabel(entity.getLabel());
        addressResponseDTO.setAddressLine1(entity.getAddressLine1());
        addressResponseDTO.setAddressLine2(entity.getAddressLine2());
        addressResponseDTO.setLandmark(entity.getLandmark());
        addressResponseDTO.setCity(entity.getCity());
        addressResponseDTO.setState(entity.getState());
        addressResponseDTO.setPincode(entity.getPincode());
        addressResponseDTO.setSelected(entity.isSelected());
        return addressResponseDTO;
    }
}
