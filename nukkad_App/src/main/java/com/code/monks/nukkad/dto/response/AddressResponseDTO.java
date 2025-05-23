package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.AddressEntity;
import com.code.monks.nukkad.enums.AddressLabel;
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
    private AddressLabel label;

    public static AddressResponseDTO fromEntity(AddressEntity address) {
        AddressResponseDTO addressResponseDTO = new AddressResponseDTO();
        addressResponseDTO.setAddressLine1(address.getAddressLine1());
        addressResponseDTO.setAddressLine2(address.getAddressLine2());
        addressResponseDTO.setLandmark(address.getLandmark());
        addressResponseDTO.setCity(address.getCity());
        addressResponseDTO.setState(address.getState());
        addressResponseDTO.setPincode(address.getPincode());
        addressResponseDTO.setLabel(address.getLabel());
        return  addressResponseDTO;
    }
}
