package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.StorekeeperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllStorekeepersResponseDTO {

    private Long id;
    private String storeName;

    public static GetAllStorekeepersResponseDTO convertToDTO(StorekeeperEntity storekeeper) {
        return new GetAllStorekeepersResponseDTO(
                storekeeper.getId(),
                storekeeper.getStoreName()
        );
    }
}
