package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.StorekeeperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateStorekeeperResponseDTO {

    private Long id;
    private String name;
    private String storeName;
    private String storeNumber;
    private String contactNumber;
    private String gstIn;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String pincode;

    public static CreateStorekeeperResponseDTO fromEntity(StorekeeperEntity entity){
        CreateStorekeeperResponseDTO dto = new CreateStorekeeperResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setStoreName(entity.getStoreName());
        dto.setStoreNumber(entity.getStoreNumber());
        dto.setContactNumber(entity.getContactNumber());
        dto.setGstIn(entity.getGstIn());
        dto.setAddressLine1(entity.getAddressLine1());
        dto.setAddressLine2(entity.getAddressLine2());
        dto.setCity(entity.getCity());
        dto.setState(entity.getState());
        dto.setPincode(entity.getPincode());
        return dto;
    }
}

