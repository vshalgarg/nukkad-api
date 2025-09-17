package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.StorekeeperEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetMyStoreByIdResponseDTO {
    private String storeName;
    private String storeQrId;

    public static GetMyStoreByIdResponseDTO fromEntity(StorekeeperEntity entity){
        GetMyStoreByIdResponseDTO dto = new GetMyStoreByIdResponseDTO();
        dto.setStoreName(entity.getStoreName());
        dto.setStoreQrId(entity.getStoreQrId());
        return dto;
    }
}
