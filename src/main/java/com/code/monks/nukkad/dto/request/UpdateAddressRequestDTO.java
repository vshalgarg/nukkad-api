package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.entities.AddressEntity;
import lombok.Data;

@Data
public class UpdateAddressRequestDTO {
    private String name;
    private String addressLine1;
    private String addressLine2;
    private String landmark;
    private String city;
    private String state;
    private String pincode;
    private String mobileNumber;

    public static void updateEntity(AddressEntity address, UpdateAddressRequestDTO dto) {
        address.setName(dto.getName());
        address.setAddressLine1(dto.getAddressLine1());
        address.setAddressLine2(dto.getAddressLine2());
        address.setLandmark(dto.getLandmark());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPincode(dto.getPincode());
        address.setMobileNumber(dto.getMobileNumber());
    }

}
