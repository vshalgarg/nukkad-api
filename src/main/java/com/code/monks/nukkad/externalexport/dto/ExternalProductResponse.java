package com.code.monks.nukkad.externalexport.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExternalProductResponse {

    private boolean success;
    private List<ExternalProduct> products;
}
