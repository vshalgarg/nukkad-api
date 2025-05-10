package com.neepanlokInfotech.nukkad_App.dto;

import lombok.Data;

import java.util.List;
@Data
public class GetCategoryDTO {
    private Long categoryId;
    private String categoryName;
    private List<ItemShortDTO> items;

    public static fromDbDto(DbDto) {

    }

    public static toDbDto()
}
