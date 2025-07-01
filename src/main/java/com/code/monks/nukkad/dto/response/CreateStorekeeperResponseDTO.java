package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.StorekeeperEntity;
import com.code.monks.nukkad.enums.RoleEnum;
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
    private String mobileNumber;
    private String gstIn;
    private String addressLine1;
    private String addressLine2;
    private String landmark;
    private String city;
    private String state;
    private String pincode;
    private RoleEnum role;
    private String storeQrId;
    private List<String> imageUrls;

    public static CreateStorekeeperResponseDTO fromEntity(StorekeeperEntity entity, List<String> imageUrls){
        CreateStorekeeperResponseDTO dto = new CreateStorekeeperResponseDTO();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setStoreName(entity.getStoreName());
        dto.setMobileNumber(entity.getMobileNumber());
        dto.setGstIn(entity.getGstIn());
        dto.setAddressLine1(entity.getAddressLine1());
        dto.setAddressLine2(entity.getAddressLine2());
        dto.setLandmark(entity.getLandmark());
        dto.setCity(entity.getCity());
        dto.setState(entity.getState());
        dto.setPincode(entity.getPincode());
        dto.setStoreQrId(entity.getStoreQrId());
        dto.setRole(entity.getRole());
        dto.setImageUrls(imageUrls);

        return dto;
    }
}

