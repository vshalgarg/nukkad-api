package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.AddressEntity;
import lombok.Data;

@Data
public class AddressResponseDTO {
    private Long id;
    private String label;
    private String addressLine1;
    private String addressLine2;
    private String landmark;
    private String city;
    private String state;
    private String pincode;


    public static AddressResponseDTO fromEntity(AddressEntity address) {
        AddressResponseDTO addressResponseDTO = new AddressResponseDTO();
        addressResponseDTO.setLabel(address.getLabel());
        addressResponseDTO.setAddressLine1(address.getAddressLine1());
        addressResponseDTO.setAddressLine2(address.getAddressLine2());
        addressResponseDTO.setLandmark(address.getLandmark());
        addressResponseDTO.setCity(address.getCity());
        addressResponseDTO.setState(address.getState());
        addressResponseDTO.setPincode(address.getPincode());
        return  addressResponseDTO;
    }
}
