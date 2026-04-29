package com.code.monks.nukkad.dto.jsonUpload;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {


    @NotBlank(message = "Category name cannot be blank")
    @Size(max = 255, message = "Category name exceeds 255 characters")
    private String categoryName;

    //  Change ADDED — optional category image URL
    private String imageUrl;


    @NotEmpty(message = "Each category must have at least one product")
    @Size(max = 1000, message = "Max 1000 products per category")

    private List<@Valid Products> products;
}
