package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.enums.UnitEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class UpdateItemRequestDTO {

    @NotBlank(message = "Item name is mandatory")
    private String name;

    @NotNull(message = "Unit is mandatory")
    private UnitEnum unit;

    private List<String> imageUrls;

    @NotNull(message = "At least one category ID is required")
    private List<Long> categoryIds;



}
