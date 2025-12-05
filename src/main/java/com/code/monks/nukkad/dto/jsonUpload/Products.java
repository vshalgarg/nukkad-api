package com.code.monks.nukkad.dto.jsonUpload;

import com.code.monks.nukkad.enums.UnitEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Products {

    private String name;
    private UnitEnum unit;
    private List<String> imageUrls;
}
