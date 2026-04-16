package com.code.monks.nukkad.dto.jsonUpload;

import jakarta.validation.Valid;
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
public class Categories {

    @NotNull(message = "Categories list can not be null")
    @NotEmpty(message = "Categories list cannot be empty")
    @Size(max = 500, message = "Max 500 categories allowed per import")
    private List<@Valid Category> categories;
}
