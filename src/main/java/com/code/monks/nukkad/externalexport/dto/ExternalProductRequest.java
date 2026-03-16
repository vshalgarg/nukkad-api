package com.code.monks.nukkad.externalexport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExternalProductRequest {

    private int category;
    private String brand = "";
    private int minPrice = 0;
    private int maxPrice = 10000;
    private int minDiscount = 0;
    private int maxDiscount = 100;
    private int page;
    private Map<String, Integer> sort;
    private int limit;
}

