package com.code.monks.nukkad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressSnapshotDTO {
    private String name;
    private String mobileNumber;
    private String addressLine1;
    private String addressLine2;
    private String landmark;
    private String city;
    private String state;
    private String pincode;
}
