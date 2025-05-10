package com.neepanlokInfotech.nukkad_App.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter @Setter
public class ItemResponseDTO {
    private Long itemId;
    private String itemName;
    private String image;
    private String unit;
    private List<Long> categoryIds;
}
