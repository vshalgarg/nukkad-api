package com.code.monks.nukkad.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ItemExcelDTO {

    private String name;
    private String unit;
    private List<Long> categoryIds;
    private List<String> imageUrls;
}
