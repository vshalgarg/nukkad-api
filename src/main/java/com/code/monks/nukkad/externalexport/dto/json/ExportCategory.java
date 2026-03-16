package com.code.monks.nukkad.externalexport.dto.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportCategory {
    private String categoryName;
    private List<ExportProduct> products;
}
