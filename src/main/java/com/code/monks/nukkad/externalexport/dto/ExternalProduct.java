package com.code.monks.nukkad.externalexport.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExternalProduct {

    private Integer id;
    private String name;
    private String recommendedAttribute;
    private List<String> images;
}
