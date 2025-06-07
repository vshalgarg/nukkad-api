package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.entities.AddressEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateAddressRequestDTO {

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


    public void updateEntity(AddressEntity address) {
        address.setLabel(label);
        address.setAddressLine1(addressLine1);
        address.setAddressLine2(addressLine2);
        address.setLandmark(landmark);
        address.setCity(city);
        address.setState(state);
        address.setPincode(pincode);
    }

}
