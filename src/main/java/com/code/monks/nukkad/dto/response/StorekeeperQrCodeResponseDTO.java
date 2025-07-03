package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.StorekeeperQrCodeEntity;
import lombok.Data;

@Data
public class StorekeeperQrCodeResponseDTO {
    private Long id;
    private String qrImageUrl;
    private boolean isDefault;

    public static StorekeeperQrCodeResponseDTO fromEntity(StorekeeperQrCodeEntity entity) {
        StorekeeperQrCodeResponseDTO dto = new StorekeeperQrCodeResponseDTO();
        dto.setId(entity.getId());
        dto.setQrImageUrl(entity.getQrImageUrl());
        dto.setDefault(entity.isDefault());
        return dto;
    }

}
