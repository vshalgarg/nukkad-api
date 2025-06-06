package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.entities.AddressEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateAddressRequestDTO {


    private String label;

    @NotBlank(message = "Address Line 1 is required")
    private String addressLine1;

    private String addressLine2;

    private String landmark;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Pincode is required")
    private String pincode;


    public static AddressEntity toEntity(CreateAddressRequestDTO dto) {
        AddressEntity address = new AddressEntity();
        address.setLabel(dto.getLabel());
        address.setAddressLine1(dto.getAddressLine1());
        address.setAddressLine2(dto.getAddressLine2());
        address.setLandmark(dto.getLandmark());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPincode(dto.getPincode());
        return address;
    }
}
