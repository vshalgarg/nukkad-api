package com.code.monks.nukkad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddStoreResponseDto {
    private  Long storekeeperId;
    private String storeKeeperName;
    private String storeName;
    private String addressLine1;
    private String addressLine2;
    private String message;

    public AddStoreResponseDto(String message){
        this.message = message;
    }
}
