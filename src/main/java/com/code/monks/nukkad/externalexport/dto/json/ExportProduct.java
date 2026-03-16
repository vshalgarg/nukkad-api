package com.code.monks.nukkad.externalexport.dto.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportProduct {
    private String name;
    private String unit;
    private List<String> imageUrls;
}

