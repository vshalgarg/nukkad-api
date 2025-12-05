package com.code.monks.nukkad.dto.jsonUpload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Categories {
    private List<Category> categories;
}
