package com.code.monks.nukkad.dto.jsonUpload;

import com.code.monks.nukkad.enums.UnitEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Products {

    @NotBlank(message = "Product name cannot be blank")
    @Size(max = 255, message = "Product name exceeds 255 characters — DB column is varchar(255)")
    private String name;

    @NotNull(message = "Unit cannot be null. Valid: WEIGHT(KG,GM) | VOLUME(L,ML) | PACKET(PKT)")
    private UnitEnum unit;

    @NotEmpty
    private List<String> imageUrls;
}
