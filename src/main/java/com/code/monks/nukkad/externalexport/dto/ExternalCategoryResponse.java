package com.code.monks.nukkad.externalexport.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExternalCategoryResponse {

    private boolean success;
    private List<ExternalCategory> category;
}
