package com.neepanlokInfotech.nukkad_App.dto;

import lombok.Data;

import java.util.List;
@Data
public class GetCategoryDTO {
    private Long id;
    private String name;
    private List<ItemShortDTO> items;

}
