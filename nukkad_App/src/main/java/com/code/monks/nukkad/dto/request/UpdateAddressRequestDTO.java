package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.enums.AddressLabel;
import lombok.Data;

@Data
public class UpdateAddressRequestDTO {

    private AddressLabel label;
    private String addressLine1;
    private String addressLine2;
    private String landmark;
    private String city;
    private String state;
    private String pincode;

}
