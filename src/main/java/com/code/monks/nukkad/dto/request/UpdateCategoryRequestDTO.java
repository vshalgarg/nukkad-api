package com.code.monks.nukkad.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateCategoryRequestDTO {

    @NotBlank(message = "category name is mandatory")
    private String name;

    @NotBlank(message = "Image url is mandatory")
    private String imageUrl;
}
