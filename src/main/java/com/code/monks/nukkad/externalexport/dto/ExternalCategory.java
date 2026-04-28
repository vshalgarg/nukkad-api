package com.code.monks.nukkad.externalexport.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExternalCategory {

    private String _id;
    private String name;
    private int id;
    private List<String> images;
}
