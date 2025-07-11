package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.entities.StorekeeperEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetStorekeeperProfileResponseDTO {

    private Long id;
    private String name;
    private String storeName;
    private String gstIn;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String pincode;
    private String mobileNumber;
    private String storeQrId;
    private List<String> imageUrls;

    public static GetStorekeeperProfileResponseDTO fromEntity(StorekeeperEntity entity) {
        return GetStorekeeperProfileResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .storeName(entity.getStoreName())
                .gstIn(entity.getGstNum())
                .addressLine1(entity.getAddressLine1())
                .addressLine2(entity.getAddressLine2())
                .city(entity.getCity())
                .state(entity.getState())
                .pincode(entity.getPincode())
                .mobileNumber(entity.getMobileNumber())
                .storeQrId(entity.getStoreQrId())
                .imageUrls(
                        entity.getImages() != null ?
                                entity.getImages().stream()
                                        .map(img -> img.getImageUrl())
                                        .collect(Collectors.toList()) :
                                List.of()
                )
                .build();
    }
}
