package com.code.monks.nukkad.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class BulkCreateCategoryRequestDTO {
    private List<CreateCategoryRequestDTO> categories;
}
