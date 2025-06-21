package com.code.monks.nukkad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class GetMyStoreResponseDto {
    private Long id;
    private String name;
    private String storeName;
    private String mobileNumber;
    private String gstIn;
    private String addressLine1;
    private String addressLine2;
    private String landmark;
    private String city;
    private String state;
    private String pincode;
    private String storeId;
}
