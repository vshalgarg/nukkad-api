package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.entities.AddressEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAddressRequestDTO {


    private String label;
    private String addressLine1;
    private String addressLine2;
    private String landmark;
    private String city;
    private String state;
    private String pincode;

    public static AddressEntity toEntity(CreateAddressRequestDTO dto){
        AddressEntity address = new AddressEntity();
        address.setLabel(dto.getLabel());
        address.setAddressLine1(dto.getAddressLine1());
        address.setAddressLine2(dto.getAddressLine2());
        address.setLandmark(dto.getLandmark());
        address.setCity(dto.getCity());
        address.setState((dto.getState()));
        address.setPincode(dto.getPincode());
        return address;

    }
}
