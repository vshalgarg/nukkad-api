package com.code.monks.nukkad.dto.category;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter@Setter
public class PaginatedCategoryResponse {
    private List<CategoryDto> content;
    private int totalPages;
    private long totalElements;
    private int number;
    private int size;
}
