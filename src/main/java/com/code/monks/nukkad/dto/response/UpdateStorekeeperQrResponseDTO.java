package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.StorekeeperQrCodeEntity;
import lombok.Data;

@Data
public class UpdateStorekeeperQrResponseDTO {
    private Long id;
    private String qrImageUrl;
    private boolean isDefault;

    public static UpdateStorekeeperQrResponseDTO fromEntity(StorekeeperQrCodeEntity entity) {
        UpdateStorekeeperQrResponseDTO dto = new UpdateStorekeeperQrResponseDTO();
        dto.setId(entity.getId());
        dto.setQrImageUrl(entity.getQrImageUrl());
        dto.setDefault(entity.isDefault());
        return dto;
    }

}
