package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.enums.AddressLabel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateAddressRequestDTO {

    private AddressLabel label;

    @NotBlank private String addressLine1;
    private String addressLine2;
    private String landmark;

    @NotBlank private String city;
    @NotBlank private String state;
    @NotBlank private String pincode;
}
